
Containing information for creating a Docker Jenkins image
==========================================================

Intended for following usage
* Java / JDK
* Maven
* Using Volvo Maven repo / settings






References
==========
https://github.com/jenkinsci/docker/issues/471
https://stackoverflow.com/questions/46052711/extend-jenkins-image-to-install-maven
https://github.com/mkheck/jenkins/blob/master/Dockerfile
https://dzone.com/articles/dockerizing-jenkins-2-setup-and-using-it-along-wit
https://gist.github.com/ivan-pinatti/de063b610d1bdf2da229c7874968f4d9


git clone 
ssh://yt52878@git.it.volvo.net:29418/Test_Manager

Status
======

Working
* Java / JDK is working
* Maven is installed. mvn -v working
* Install Jenkins plugins from a list during docker build
* Git command works, is in base image
* Git SCM Jenkins plugin installed
* Works to clone repo if you give git repo credentials (user, pass and private SSH key)
* Using Volvo maven settings.xml file and accessing Volvo maven repo, Nexus.

Open issues
* Git access, better with SSH keys. https://gist.github.com/ivan-pinatti/de063b610d1bdf2da229c7874968f4d9
* Be able to do docker builds
* Look into optimizing Java and Maven installation, see https://dzone.com/articles/dockerizing-jenkins-2-setup-and-using-it-along-wit.


