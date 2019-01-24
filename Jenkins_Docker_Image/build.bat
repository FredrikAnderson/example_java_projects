
docker build --build-arg=http_proxy=%HTTP_PROXY% . --tag=fredrik/jenkins 

rem docker build --no-cache --build-arg=http_proxy=%HTTP_PROXY% . --tag=fredrik/jenkins 
