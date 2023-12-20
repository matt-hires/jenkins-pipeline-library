pipeline {
    agent any

    options {
        buildDiscarder(
                logRotator(
                        artifactDaysToKeepStr: '',
                        artifactNumToKeepStr: '',
                        daysToKeepStr: '',
                        numToKeepStr: '5'
                        )
        )

        ansiColor('xterm')
        skipDefaultCheckout()

        disableConcurrentBuilds()
    }

    tools {
        maven 'Maven'
        jdk 'java-17'
    }

    environment {
        MASTER_BRANCH_REGEX = '(master|main|release.*)'
    }

    stages {
        stage('Prepare Workspace') {
            steps {
                deleteDir()
                checkout scm

                updateGitlabCommitStatus name: 'build', state: 'pending'
            }
        }
        stage('Build') {
            steps {
                mvnCmd('clean package')
            }
        }
        stage('Deploy') {
            when {
                branch pattern: env.MASTER_BRANCH_REGEX, comparator: 'REGEXP'
            }
            steps {
                mvnCmd('deploy')
            }
        }
    }
    post {
        success {
            updateGitlabCommitStatus(name: 'build', state: 'success')
        }
        aborted {
            updateGitlabCommitStatus(name: 'build', state: 'canceled')
        }
        failure {
            script {
                updateGitlabCommitStatus(name: 'build', state: 'failed')

                emailext(
                        attachLog: false,
                        body: "Build failed: ${BUILD_URL}",
                        subject: 'Build failed: ${JOB_NAME}/${BUILD_NUMBER}',
                        recipientProviders: [
                            buildUser()
                        ]
                )
            }
        }
        unstable {
            script {
                updateGitlabCommitStatus(name: 'build', state: 'success')

                emailext(
                        attachLog: false,
                        body: "Build failed: ${BUILD_URL}",
                        subject: 'Build failed: ${JOB_NAME}/${BUILD_NUMBER}',
                        recipientProviders: [
                            buildUser()
                        ]
                )
            }
        }
    }
}
