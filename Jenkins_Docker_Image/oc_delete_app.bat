
oc login --username=yt52878 --password=IlJ2019 acps-admin-alpha.srv.volvo.com:8443 --insecure-skip-tls-verify

oc project edb-dev

oc delete all -l app=jenkins -n edb-dev
