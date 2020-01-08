import jenkins.model.*


def instance = Jenkins.getInstance()

instance.setNumExecutors(3)


final String name = "cloudpxgot1.srv.volvo.com"
final int port = 8080
final String userName = ""
final String password = ""
final String noProxyHost = "localhost,.volvo.net,.volvo.com"

final def pc = new hudson.ProxyConfiguration(name, port, userName, password, noProxyHost)
instance.proxy = pc
instance.save()
pc.save()

println "Proxy settings updated!"

