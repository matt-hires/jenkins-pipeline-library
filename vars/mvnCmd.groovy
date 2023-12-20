def call(params) {

    def disablePublishers = false
    def returnStatus = false
    def returnStdout = false
    String cmdLine

    if (params instanceof String) {

        cmdLine = params
    } else {

        cmdLine = params.get('cmdLine')
        disablePublishers = params.get('disablePublishers', false)
        returnStatus = params.get('returnStatus', false)
        returnStdout = params.get('returnStdout', false)
    }

    def options = []

    if (disablePublishers) {
        options = [artifactsPublisher(disabled: true),
                   junitPublisher(disabled: true),
                   findbugsPublisher(disabled: true),
                   jacocoPublisher(disabled: true),
                   openTasksPublisher(disabled: true),
                   jgivenPublisher(disabled: true)]
    }

    withMaven(options: options, traceability: false) {

        // workaround ([ERROR] Picked up JAVA_TOOL_OPTIONS)
        // https://github.com/flowlogix/jenkins/blob/2c195907ce0de280cdc843a23712f5192bcc5ce0/UnitTests.groovy#L30
        // https://issues.jenkins.io/browse/JENKINS-65528?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel
        def returnVal = sh(script:
                """export MAVEN_OPTS="\$MAVEN_OPTS $JAVA_TOOL_OPTIONS"
                   unset JAVA_TOOL_OPTIONS
                   mvn $cmdLine""",
                returnStatus: returnStatus, returnStdout: returnStdout)

        if (returnStdout) {
            returnVal = returnVal.trim()
        }

        return returnVal
    }
}

def getLatestArtifactReleaseVersion(params = [:]) {

    def searchParams = [:]
    searchParams['nexusUrl'] = params['nexusUrl']
    searchParams['groupId'] = params['groupId']
    searchParams['artifactId'] = params['artifactId']
    searchParams['repository'] = 'releases'

    def jsonSearchResult = searchNexus(searchParams)

    return getFirstArtifactFromSearchResult(jsonSearchResult)
}

def getFirstArtifactFromSearchResult(jsonSearchResult) {

    def artifacts = jsonSearchResult["items"] as ArrayList
    // das aktuellste artefakt sollte das erste in der liste sein
    def firstArtifact = artifacts.size() > 0 ? artifacts.get(0) : null

    if (!firstArtifact) {

        return null
    }

    return firstArtifact["maven2"]["version"]
}

def getLatestArtifactSnapshotVersion(params = [:]) {

    def searchParams = [:]
    searchParams['nexusUrl'] = params['nexusUrl']
    searchParams['groupId'] = params['groupId']
    searchParams['artifactId'] = params['artifactId']
    searchParams['extension'] = params.get('extension')
    searchParams['classifier'] = params.get('classifier', '')

    searchParams['repository'] = 'snapshots'

    def artifacts = searchNexus(searchParams)

    return getFirstArtifactFromSearchResult(artifacts)
}

def searchNexus(params = [:]) {

    def nexusUrl = params['nexusUrl']
    def groupId =  params['groupId']
    def artifactId = params['artifactId']
    def repository = params['repository']
    def extension = params.get('extension', 'pom')
    def classifier = params.get('classifier', '')

    def nexusRestEndpoint = "$nexusUrl/service/rest/v1/search/assets"

    def status = sh(
            script: "curl -sX GET \"${nexusRestEndpoint}?sort=version&direction=desc&repository=${repository}"
                    + '&maven.groupId=' + groupId
                    + '&maven.artifactId=' + artifactId
                    + '&maven.classifier=' + classifier
                    + "&maven.extension=${extension}\""
                    + " -H \"accept: application/json\" --output ${artifactId}.json",
            returnStatus: true
    )

    if (status > 0) {

        error("Reading artifact version from nexus failed. Curl exit code: $status")
    }

    return readJSON(file: "${artifactId}.json")
}

def readProperty(String propertyName) {

    def propertyValue = call(
            cmdLine: '-q -Dexec.executable=echo -Dexec.args=\'${' + propertyName + '}\' --non-recursive exec:exec',
            disablePublishers: true,
            returnStatus: false,
            returnStdout: true)

    if (!propertyValue) {

        error("Error while getting property: ${propertyName}")
    }

    return propertyValue
}
