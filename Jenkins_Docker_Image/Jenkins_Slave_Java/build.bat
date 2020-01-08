
copy ..\settings.xml .
copy ..\oc .

docker build --build-arg=http_proxy=%HTTP_PROXY% --build-arg=home=%HOME% . --tag=fredrik/jenkins-slave

@rem docker build --no-cache --build-arg=http_proxy=%HTTP_PROXY% . --tag=fredrik/jenkins 
