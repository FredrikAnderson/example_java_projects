
oc login --username=yt52878 acps-admin-alpha.srv.volvo.com:8443 --insecure-skip-tls-verify

oc project wds-test

oc create imagestream jenkins

oc tag maven2.it.volvo.net:18471/fredrik/jenkins:latest jenkins:latest

oc new-app jenkins:latest --name=jenkins

oc set resources deploymentconfigs jenkins --requests=memory=256Mi --limits=memory=1500Mi

oc expose svc/jenkins --port=8080

oc apply -f networkpolicy.yaml
