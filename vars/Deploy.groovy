import should.*

def call(body) {

    if (should.deploy(env.BRANCH_NAME) == true){
        script {
             sh "/opt/kubectl-patch.sh ${env.PACKAGE_NAME} ${env.PACKAGE_VERSION} ${env.BRANCH_NAME}"
        }
    }
    if (env.BRANCH_NAME == "master"){
        script {
             sh "git fetch"
	     sh "git tag ${env.PACKAGE_VERSION}"
	     sh "git push --tags"
	     sh "git push origin --delete release/${env.PACKAGE_VERSION}"	
        }
    }
}

