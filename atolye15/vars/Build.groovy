import should.*

def call(body) {

 if(should.test(env.BRANCH_NAME) == true){
	sh "yarn"
 }

}

