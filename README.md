# Jenkins Pipeline Utilities
  
Global Jenkins Pipeline Library with common utilities.  
For basic instructions, see [Usage](#usage) and [Configuration](#configuration) sections below.

Defines the following global [steps](#steps):
* [`withDockerEx`](#withdockerex)
* [`waitForPort`](#waitforport)
* [`waitForEndpoint`](#waitforendpoint)
* [`withUsernamePassword`](#withusernamepassword)
* [`withSecretText`](#withsecrettext)
* [`withSecretFile`](#withsecretfile)
* [`failAsUnstable`](#failasunstable)
* [`shEx`](#shex)
* [`interactiveShell`](#interactiveshell)
* [`interactiveGroovyShell`](#interactivegroovyshell)

## Usage

**Basic `Jenkinsfile` Example:**
```groovy
@Library('pipeline-utils@1.x') _

properties([
  discardOldBuildsProperty()
])

withCommonPipelineSettings {
  node('node-with-docker-compose') {
    stage('Setup') {
      checkout scm
    }
        
    stage('Build') {
      // Build ...
    }
        
    stage('Unit Tests') {
      // Unit tests ...
    }
        
    stage('Publish') {
      // Publish ...
    }
        
    failAsUnstable {
      withDockerEx { dockerEx ->
        def sidecarIp
        stage('Start Sidecars') {
          def sidecar = dockerEx.compose('sidecar', file: 'path/to/sidecar.yml')
          sidecar.up().ps()
          sidecarIp = sidecar.inspectGateway()
          echo "Sidecar IP: $sidecarIp"
        }
                
        stage('Integration Tests') {
          // Run integration tests using the sidecar IP
        }
      }
    }
  }
}
```


### Configuration
After that, you can include this library from your jenkins pipeline script:

```#!groovy
@Library('jenkins-utils') _
```  

#### `withDockerEx`
Start a docker extension context.
  
**Example:**
```groovy
withDockerEx { dockerEx ->
  // Assuming there is a docker compose file named 'my-project.yml' in the current directory 
  def myProj = dockerEx.compose('my-project', [file: 'my-project.yml'])
  // Spin the composition up and print its status
  myProj.up().ps()
  // Get the public gateway IP of the composition
  def myProjIp = myProj.inspectGateway()
  // Wait for a specific port in the composition to be responsive
  waitForPort host: myProjIp, port: 8080
  
  // Do something with the composition ...
  
  // Stop and remove the composition
  myProj.down()
}
```
*Note-*  
`withDockerEx` closes its context automatically when the closure ends 
(this includes stopping and removing the composition) 
so calling `myProj.down()` explicitly is not really necessary.

**Requirements:**
* `docker`
* `docker-compose`

#### `waitForPort`
Wait for a port to be responsive or for a timeout.

**Arguments:**
* `host` - (String) the host or IP to check
* `port` - (int) the port to check
* `timeout` - (Map) the timeout settings. See [basic timeout step documentation](https://jenkins.io/doc/pipeline/steps/workflow-basic-steps/#code-timeout-code-enforce-time-limit) for more details.  
  (optional, default: `[time: 20, unit: 'SECONDS']`)
  
**Example:**
```groovy
waitForPort host: 'the-host', port: 1234
```

**Requirements:**
* `nc` (NetCat)

#### `waitForEndpoint`
Wait for an HTTP endpoint to be responsive. 
Issues a GET request to a given URL endpoint and waits until it responds (no matter the response status). 

**Arguments:**
* `url` - (String) the URL endpoint to check
* `timeout` - (Map) the timeout settings. See [basic timeout step documentation](https://jenkins.io/doc/pipeline/steps/workflow-basic-steps/#code-timeout-code-enforce-time-limit) for more details.  
  (optional, default: `[time: 20, unit: 'SECONDS']`)
* `requestTimeoutSec` - (int) the max time in seconds for each attempt.  
  (optional, default: `3`)

**Example:**
```groovy
waitForEndpoint url: 'http://www.acme.com'
```

**Requirements:**
* `curl`

#### `withUsernamePassword`
Get username and password from a stored jenkins credentials and use them.

**Arguments:**
* `credentialsId` - (String) the ID of the credentials to use

**Example:**
```groovy
withUsernamePassword(credentialsId: 'the-creds-id') { username, password ->
  // Do something with the username and password...
}
```

#### `withSecretText`
Get a secret text by credentials ID and use it.

**Arguments:**
* `credentialsId` - (String) the ID of the credentials to use

**Example:**
```groovy
withSecretText(credentialsId: 'the-secret-creds-id') { secretText ->
  // Do something with the secret
}
```

#### `withSecretFile`
Get a secret file path by credentials ID and use it.

**Arguments:**
* `credentialsId` - (String) the ID of the credentials to use

**Example:**
```groovy
withSecretFile(credentialsId: 'the-secret-creds-id') { secretFilepath ->
  // Do something with the secret file
  def secret = readFile(file: secretFilepath)
}
```

#### `failAsUnstable`
Defines a scope in which in case of failure it is reported as `UNSTABLE`.

**Example:**
```groovy
failAsUnstable {
  // Do something...
}
``` 

*Note-* This step can be used only in the end of the pipeline. 
Further steps after the `failAsUnstable` scope is closed will be executed and hence might affect the result status.

#### `shEx`
Executes a shell script and returns a map with both stdout and stderr, and also the result status.  
Returns an `ArrayMap` with the following keys: 'out' (String), 'err' (String), 'status' (int).

**Arguments:**
* `script` - (String) the script to execute

**Example:**
```groovy
def result = shEx(script: 'java -version')
echo "out: ${result.get('out')}, err: ${result.get('err')}, status: ${result.get('status')}"
```

#### `withRetry`
Execute a code block and retry in case of an error.  
If all attempts fail, the error of the last attempt is thrown.

**Arguments:**  
* `retries` - (int) the number of retry attempts  
  (optional, default: `3`)

**Example:**
```groovy
withRetry(retries: 5) {
  // Do something that might fail
}
```

#### `interactiveShell`
Starts an interactive shell loop where the build execution is paused and the user can interactively 
execute shell command (implemented using the `sh` basic step). 
To break the loop enter the `exit` command. The interactive shell has a total timeout of 10 minutes.  
*Note-* This step shall not be part of a real pipeline. 
Its goal is to assist with pipeline development and debugging.

**Example:**
```groovy
interactiveShell()
//Pauses the pipeline execution and waits for human interaction
```

#### `interactiveGroovyShell`
Starts an interactive groovy shell loop where the build execution is paused and the user can interactively 
execute groovy script (implemented using the `load` basic step). 
To break the loop enter `exit` as the script. The interactive groovy shell has a total timeout of 10 minutes.  
*Note-* This step shall not be part of a real pipeline. 
Its goal is to assist with pipeline development and debugging.  

**Example:**
```groovy
interactiveGroovyShell()
//Pauses the pipeline execution and waits for human interaction
```

----
  
Copyright 2018 eBay Inc.  
