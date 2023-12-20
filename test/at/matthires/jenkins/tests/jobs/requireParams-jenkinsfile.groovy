package at.matthires.jenkins.tests.jobs

pipeline {
    agent none

    parameters {
        string(name: 'MANDATORY_PARAM')
    }

    stages {
        stage('Test') {
            script {
                requireParam(params.MANDATORY_PARAM, "MANDATORY_PARAM")
            }
        }
    }
}
