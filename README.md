## Step 1 - ADD PLUGIN
> GitHub
> Github-Integration
> Blue Ocean


## Step 2 - ADD NEW ITEM  
  
Enter an item name: GitHubRepos  
Type: Github Organization  
  
Display Name: GitHubRepos  
GitHub Organization: Add your github account username and password  
Owner: your username  
Behaviors:  
  
--Repository--  
>Filter by name (with regular expression)  
>>.*(atolye15|jenkins)+.*  
  
--Within repository--  
>Discover branches  
>>Exclude branches that are also filed as PRs  
  
>Discover pull requests from origin  
>>Merging the pull requests with the current target  
  
>Discover pull requests from forks  
>>Strategy: Merging the pull requests with the current target  
>>Trust: From users with Admin or Write permission  
  
>Discover Tags  
  
>Filter by name (with wildcards)  
Include: master release* develop hotfix* PR*  
Exclude: feature*  
  
--General--  
Checkout over SSH  
Credentials: Add Jenkins -> SSH Username with private key  
Username: your jenkins username  
Private Key: Your ~/.ssh/id_rsa  
Passphrase: If you have id_rsa file pass  
  
  
Pipeline Jenkinsfile  
Script Pat: Jenkinsfile  
  
Others: default  
  
  
## Step 3 - MANAGE JENKINS -- CONFIGURE SYSTEM  
  
Jenkins URL	http://51.158.181.245:8080/  
  
Global Pipeline Libraries  
>Library   
>>Name : jenkins-utils  
>>Default Version : master  
>>Load implicitly: False  
>>Allow default version to be overridden: True  
>>Include @Library changes in job recent changes: True  
  
Retrieval method  
>Modern SCM: True  
Source Code Management  
>Source Code Management  
>>Project Repository : git@github.com:ahmetyuksel/jenkins-utils.git  
>>Credentials : select user which you entered repo configure (at over SSH section)  
>>Behaviors  
>>Add > Discover branches and Discover tags  
