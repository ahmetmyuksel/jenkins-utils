def call(body) {
        try{
           deleteDir()
        }catch(Exception e){
           echo e.toString()
        }

        checkout scm

        sh script: "/opt/version", label: "Detect new version for packaging"

        try {
          def BUILD_CONTEXT = readProperties file:'BUILD_CONTEXT_FILE'
          env['PACKAGE_NAME'] = BUILD_CONTEXT['PACKAGE_NAME']
          env['PACKAGE_VERSION'] = BUILD_CONTEXT['PACKAGE_VERSION']
          env['GIT_URL'] = BUILD_CONTEXT['GIT_URL']
        }catch (Exception e) {
          echo e.toString()
        }

}
