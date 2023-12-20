void push(String credentialsId, String branchName) {

    withCredentials([sshUserPrivateKey(credentialsId: "$credentialsId", keyFileVariable: 'key')]) {

        sh("env GIT_SSH_COMMAND=\"ssh -i $key\" git push -u origin ${branchName}")
    }
}

void pull(String credentialsId, String branchName) {

    withCredentials([sshUserPrivateKey(credentialsId: "$credentialsId", keyFileVariable: 'key')]) {

        sh("env GIT_SSH_COMMAND=\"ssh -i $key\" git pull --rebase origin ${branchName}")
    }
}
