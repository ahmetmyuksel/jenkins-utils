import should.*

def call(body) {

 if(should.pack(env.BRANCH_NAME) == true){
	sh "yarn build"
	sh "docker build -t gcr.io/a15demo/${env.PACKAGE_NAME}:${env.PACKAGE_VERSION} . --no-cache"
        sh "docker push gcr.io/a15demo/${env.PACKAGE_NAME}:${env.PACKAGE_VERSION}"
 }

}
