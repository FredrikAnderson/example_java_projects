
oc login acps-admin-alpha.srv.volvo.com:8443 --insecure-skip-tls-verify

oc project edb-dev

oc create imagestream edbjenkins-slave

oc tag maven2.it.volvo.net:18471/fredrik/jenkins-slave:latest edbjenkins-slave:latest
