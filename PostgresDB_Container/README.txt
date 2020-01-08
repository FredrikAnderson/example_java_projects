
Creating a Postgres Database container
======================================

1. Modify oc_create_postgres.bat as per your needs.
2. Login to Openshift and set your project / namespace.
3. Execute oc_create_postgres.bat


Create port-forward:
1. oc get pod
2. Port-forward by: oc port-forward [pod_name] [localPort]:[containerPort]

Connect on local PC using JDBC URL: jdbc:postgresql://127.0.0.1:15432/db-test


https://confluence.it.volvo.net/display/JVS/PostgreSQL+in+a+container

