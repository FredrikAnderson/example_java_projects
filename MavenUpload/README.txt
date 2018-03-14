

mvn deploy:deploy-file -DgroupId=my.group.id \
    -DartifactId=my-artifact-id \
    -Dversion=1.0.0.1 \
    -Dpackaging=jar \
    -Dfile=foo.jar \
    -DgeneratePom=true \
    -DrepositoryId=my-repo \
    -Durl=http://my-nexus-server.com:8081/repository/maven-releases/
UPDATE: As stated in comments using quotes in url cause NoSuchElementException

But I have add server config in my maven (~/.m2/settings.xml).

<servers>
  <server>
    <id>my-repo</id>
    <username>admin</username>
    <password>admin123</password>
  </server>
</servers>



mvn deploy:deploy-file -DgroupId=org.apache.activemq -DartifactId=activemq-rar -Dversion=5.14.5 -Dpackaging=rar -Dfile=activemq-rar-5.14.5.rar -DgeneratePom=true -DrepositoryId=maven2.it.volvo.net -Durl=http://maven2.it.volvo.net/repository/adtjavaee/

mvn deploy:deploy-file -DgroupId=com.github.approvals -DartifactId=ApprovalTests -Dversion=0.13 -Dpackaging=jar -DgeneratePom=true -DrepositoryId=maven2.it.volvo.net -Durl=http://maven2.it.volvo.net/repository/adtjavaee/ -Dfile=ApprovalTests-0.13.jar


curl -v -u yt52878:IawTCP50 --upload-file ApprovalTests-0.13.jar http://maven2.it.volvo.net/repository/adtjavaee/com/github/approvals/ApprovalTests/0.13/ApprovalTests-0.13.jar




curl -u yt52878:IawTCP50 -X GET 'http://maven2.it.volvo.net/service/rest/beta/search/assets/download?group=com.github.approvals&name=ApprovalTests&version=0.13'


First add repo
http://maven2.it.volvo.net/repository/idcSnapshots/

then add groupId
com/volvo/vtom/changedorderpublisher/orderinfocomer/

then add artifactId
VTOM-orderinfocomer-bar/



http://maven2.it.volvo.net/swagger-ui/#!/search/searchAssets

http://maven2.it.volvo.net/service/siesta/rest/beta/search/assets?repository=idcSnapshots&format=maven2&group=com.volvo.atori.emptycache.emptycache&name=ATORI-emptycache-bar&maven.baseVersion=1.0.0-SNAPSHOT&maven.extension=bar


curl -v -u yt52878:IawTCP50 -X GET 'http://maven2.it.volvo.net/service/siesta/rest/beta/search/assets/download?repository=idcSnapshots&format=maven2&group=com.volvo.atori.emptycache.emptycache&name=ATORI-emptycache-bar&maven.baseVersion=1.0.0-SNAPSHOT&maven.extension=bar'

Gives:
* Server auth using Basic with user 'yt52878'
> GET /service/siesta/rest/beta/search/assets/download?repository=idcSnapshots&format=maven2&group=com.volvo.atori.emptycache.emptycache&name=ATORI-emptycache-bar&maven.baseVersion=1.0.0-SNAPSHOT&maven.extension=bar HTTP/1.1
> Host: maven2.it.volvo.net
> Authorization: Basic eXQ1Mjg3ODpJYXdUQ1A1MA==
> User-Agent: curl/7.48.0
> Accept: */*
>
< HTTP/1.1 302 Found
< Date: Thu, 01 Mar 2018 13:55:08 GMT
< Server: Nexus/3.7.1-02 (OSS)
< X-Frame-Options: SAMEORIGIN
< X-Content-Type-Options: nosniff
< Location: http://maven2.it.volvo.net/repository/idcSnapshots/com/volvo/atori/emptycache/emptycache/ATORI-emptycache-bar/1.0.0-SNAPSHOT/ATORI-emptycache-bar-1.0.0-20180221.124729-1.bar
< Content-Length: 0
< Connection: close
< Content-Type: text/plain; charset=UTF-8
<
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
* Closing connection 0



https://support.sonatype.com/hc/en-us/articles/213465488-How-can-I-retrieve-a-snapshot-if-I-don-t-know-the-exact-filename-

http://maven2.it.volvo.net/service/siesta/rest/beta/search/assets/download?repository=idcSnapshots&format=maven2&group=com.volvo.s05.goodsreceipt.syncgoodsreceipt&name=S05-syncgoodsreceipt-bar&maven.baseVersion=1.0.0-SNAPSHOT&maven.extension=bar

mvn dependency:get -DremoteRepositories=http://maven2.it.volvo.net/repository/idcSnapshots/ -DgroupId=com.volvo.s05.goodsreceipt.syncgoodsreceipt -DartifactId=S05-syncgoodsreceipt-bar -Dversion=1.0.0-SNAPSHOT -Dtransitive=false -Dpackaging=bar

Then use the "dependency:copy" goal to move the snapshot from your local repository to wherever you like:

mvn dependency:copy -Dartifact=org.foo:project:1.0.0-SNAPSHOT -DoutputDirectory=.

mvn dependency:copy -Dartifact=com.volvo.s05.goodsreceipt.syncgoodsreceipt:S05-syncgoodsreceipt-bar:1.0.0-SNAPSHOT:bar -DoutputDirectory=.


mvn install:install-file -DgroupId=com.volvo.s05.goodsreceipt.syncgoodsreceipt -DartifactId=S05-syncgoodsreceipt-bar -Dversion=1.0.0-SNAPSHOT -Dtransitive=false -Dpackaging=bar


mvn dependency:get -DremoteRepositories=http://maven2.it.volvo.net/repository/idcSnapshots/ -DgroupId=com.volvo.s05.goodsreceipt.syncgoodsreceipt -DartifactId=S05-syncgoodsreceipt-bar -Dversion=1.0.0-SNAPSHOT -Dtransitive=false -Dpackaging=bar
mvn dependency:copy -Dartifact=com.volvo.s05.goodsreceipt.syncgoodsreceipt:S05-syncgoodsreceipt-bar:1.0.0-SNAPSHOT:bar -DoutputDirectory=.

