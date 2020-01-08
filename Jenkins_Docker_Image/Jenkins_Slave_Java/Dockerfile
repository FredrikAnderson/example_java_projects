FROM maven2.it.volvo.net:18443/jenkins/jnlp-slave

ENV http_proxy ${http_proxy}
ENV https_proxy ${http_proxy}
ENV no_proxy localhost,.volvo.net,.volvo.com


# if we want to install via apt
USER root
# Install maven
RUN apt-get update && apt-get install -y maven


# install wget
# RUN apt-get install -y wget

# get maven version
#RUN wget --no-verbose -O /var/jenkins_home/downloads/apache-maven-3.5.4-bin.tar.gz http://apache.cs.utah.edu/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz

# Apply Volvo Nexus Maven settings
COPY settings.xml /etc/maven/

# RUN mkdir -p /var/jenkins_home/downloads 
#RUN curl -v -j -k -L -o /var/jenkins_home/downloads/jdk-8u191-linux-x64.tar.gz -H "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u191-linux-x64.tar.gz
# RUN wget -O /var/jenkins_home/downloads/jdk-8u191-linux-x64.tar.gz -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz
# RUN curl -o /var/jenkins_home/downloads/jdk-8u191-linux-x64.tar.gz http://dl.bintray.com/walkingdevs/mirrors/jdk-8.191-x64.tar.gz
# RUN curl -o /var/jenkins_home/downloads/jdk-7u80-linux-x64.tar.gz http://ftp.osuosl.org/pub/funtoo/distfiles/oracle-java/jdk-7u80-linux-x64.tar.gz
#RUN curl -o /var/jenkins_home/downloads/apache-maven-3.5.2-bin.tar.gz http://mirror.vorboss.net/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz

# Openshift oc command for interactions with Openshift
COPY oc /usr/local/bin/

RUN chgrp -R 0 /home/jenkins/ && \
    chmod -R g=u /home/jenkins/

USER jenkins

ENTRYPOINT ["jenkins-agent"]
