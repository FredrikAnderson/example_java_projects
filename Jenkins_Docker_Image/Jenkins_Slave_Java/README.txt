
Containing information for creating a Docker Jenkins image
==========================================================

Intended for following usage
* Java / JDK
* Maven
	* Using Volvo Maven repo / settings
* Support for using Git
* Support for using Openshift cli command, oc.


Docker it using:
================
build.bat

Will build a docker image containing needed stuff.

Run it using:
=============
run.bat

Will run docker image locally on dev PC.


Using Kubernetes with Jenkins Docker image i Openshift:
1. Start image / pods in Openshift
2. To enable default serviceaccount to list pods, execute: oc policy add-role-to-group view system:serviceaccounts:edb-dev
3. Add Cloud -> Kubernetes
	* Name: Kubernetes
	* Kubernetes URL: https://100.126.0.1:443, could be found from Env vars: KUBERNETES_SERVICE_HOST and port.
	* Disable HTTPS cert. check.
	* Kub. namespace: Same as Openshift project, for ex. edb-dev.
	* Test Connection!
	* Jenkins URL: Get IP of pod, check under pod, Details, IP:. Ex. http://100.126.151.37:8080
	* 


References
==========
https://github.com/jenkinsci/docker/issues/471
https://stackoverflow.com/questions/46052711/extend-jenkins-image-to-install-maven
https://github.com/mkheck/jenkins/blob/master/Dockerfile
https://dzone.com/articles/dockerizing-jenkins-2-setup-and-using-it-along-wit
https://gist.github.com/ivan-pinatti/de063b610d1bdf2da229c7874968f4d9
https://jenkins.io/doc/book/pipeline/docker/
https://www.blazemeter.com/blog/how-to-setup-scalable-jenkins-on-top-of-a-kubernetes-cluster/


Status
======

Working
* Java / JDK is working
* Maven is installed. mvn -v working
* Install Jenkins plugins from a list during docker build
* Git command works, is in base image
* Git SCM Jenkins plugin installed
* Works to clone repo. You need to define user and private key SSH credentials (user, pass and private SSH key)
* Using Volvo maven settings.xml file and accessing Volvo maven repo, Nexus.
* Look into optimizing Java and Maven installation, see https://dzone.com/articles/dockerizing-jenkins-2-setup-and-using-it-along-wit
* Openshift client support, oc command.

* 191218: Working image runs in Openshift 3.11 cluster.


Open issues
* Git access, better with SSH keys. https://gist.github.com/ivan-pinatti/de063b610d1bdf2da229c7874968f4d9
* Be able to do docker builds
* Persistent storage Openshift? How to define/add?



Verify some tools:
* Create a Freestyle job.
* Build -> Add build step -> Shell command
java -version
javac -version
mvn -v
git --version
oc version



Verify Java/Maven project:
* Add credentials with SSH username with private SSH key. Credentials -> Jenkins -> Global credentials -> Adding some credentials.
* Kind: SSH Username with private key
* ID: Jenkins-git
* Username: VCN id
* SSH private key: Paste key. Save
* Create a maven job.
* Choose SCM as Git and for example give SCM URL: ssh://yt52878@git.it.volvo.net:29418/Test_Manager
* Give ROOT pom: 7_implementation/tm/pom.xml
* Maven goal: install -DskipTests=true -pl=!tm-web




