def call(LinkedHashMap params = [:]) {

    String groupId = params.get('groupId')
    String artifactId = params.get('artifactId')
    String artifactVersion = params.get('artifactVersion')
    String artifactExtension = params.get('artifactExtension')
    String nexusUrl = params.get('nexusUrl')
    boolean snapshot = params.get('snapshot', false)

    def subPath = groupId.replace('.', '/')
    def filename = "${artifactId}-${artifactVersion}${artifactExtension}"
    def repository = snapshot ? 'snapshots' : 'releases'

    def resolvedArtifactVersion = artifactVersion
    if (snapshot) {

        resolvedArtifactVersion = artifactVersion.substring(0, artifactVersion.indexOf('-')) + '-SNAPSHOT'
    }

    def url = "${nexusUrl}/repository/${repository}/${subPath}/${artifactId}/${resolvedArtifactVersion}/${filename}"

    log.info("Fetch artifact from nexus url: ${url}")
    def httpCode = sh(
            script: "curl -s -w \"%{http_code}\" -X GET \"${url}\" --output ${filename}",
            returnStdout: true
    )

    if (httpCode != '200') {

        error("Error while getting artifact from nexus url: ${url}. Http-Status: ${httpCode}")
    }

    return filename
}
