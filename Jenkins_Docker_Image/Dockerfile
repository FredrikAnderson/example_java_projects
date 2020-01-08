FROM jenkins/jenkins:lts

# ENV http_proxy http://cloudpxgot1.srv.volvo.com:8080
# ENV https_proxy http://cloudpxgot1.srv.volvo.com:8080

ENV http_proxy ${http_proxy}
ENV https_proxy ${http_proxy}
ENV no_proxy localhost,.volvo.net,.volvo.com


# if we want to install via apt
USER root
# Install maven
RUN apt-get update && apt-get install -y maven


#RUN apt-get update && apt-get -y install apt-transport-https ca-certificates curl gnupg2 software-properties-common && \
#curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey && \
#add-apt-repository \
#   "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
#   $(lsb_release -cs) \
#   stable" && \
#apt-get update && \
#apt-get -y install docker-ce

# install wget
# RUN apt-get install -y wget

# get maven version
#RUN wget --no-verbose -O /var/jenkins_home/downloads/apache-maven-3.5.4-bin.tar.gz http://apache.cs.utah.edu/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz

# Apply Volvo Nexus Maven settings
COPY settings.xml /etc/maven/

#RUN mkdir /var/jenkins_home/.ssh
#COPY ${home}/.ssh/id_rsa /var/jenkins_home/.ssh/id_rsa



#RUN curl -v -j -k -L -o /tmp/jdk-8u191-linux-x64.tar.gz -H "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u191-linux-x64.tar.gz
#RUN curl -v -j -k -L -o /tmp/apache-maven-3.5.2-bin.tar.gz http://mirror.vorboss.net/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz

# RUN mkdir -p /var/jenkins_home/downloads 
#RUN curl -v -j -k -L -o /var/jenkins_home/downloads/jdk-8u191-linux-x64.tar.gz -H "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u191-linux-x64.tar.gz
# RUN wget -O /var/jenkins_home/downloads/jdk-8u191-linux-x64.tar.gz -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz
# RUN curl -o /var/jenkins_home/downloads/jdk-8u191-linux-x64.tar.gz http://dl.bintray.com/walkingdevs/mirrors/jdk-8.191-x64.tar.gz
# RUN curl -o /var/jenkins_home/downloads/jdk-7u80-linux-x64.tar.gz http://ftp.osuosl.org/pub/funtoo/distfiles/oracle-java/jdk-7u80-linux-x64.tar.gz
#RUN curl -o /var/jenkins_home/downloads/apache-maven-3.5.2-bin.tar.gz http://mirror.vorboss.net/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz


RUN chgrp -R 0 /var/jenkins_home && \
    chmod -R g=u /var/jenkins_home

USER jenkins

# Skip setup wizard
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

COPY groovy/* /usr/share/jenkins/ref/init.groovy.d/

# Openshift oc command for interactions with Openshift
COPY oc /usr/local/bin/

# Install default plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt


