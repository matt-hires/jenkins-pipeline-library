
void createSecret(String secretName, String password) {

    boolean secretExists = sh (script: 'podman secret ls | grep ' +  secretName, returnStatus: true) == 0

    if (secretExists) {
        sh 'podman secret rm ' + secretName
    }

    sh 'echo ' + password + ' | podman secret create ' + secretName + ' - '
}

void removeContainer(String containerName) {

    boolean containerExists = sh(script:'podman container exists ' + containerName, returnStatus: true) == 0

    if (!containerExists) {
        return
    }

    sh(script: 'podman container stop ' + containerName, returnStatus: true)

    sh('podman container rm ' + containerName)
}

void waitForOracleDbStartup(String containerName, String timeout) {

    try {

        sh 'timeout -k 30s ' + timeout +
                ' bash -c \"while ! podman exec ' +  containerName + ' /opt/oracle/checkDBStatus.sh &>/dev/null; do sleep 1m; done\"'

    } catch (exc) {
        sh(script: "podman stop $containerName", returnStatus: true)
        sh(script: "podman container rm -f $containerName", returnStatus: true)

        throw exc
    }
}
