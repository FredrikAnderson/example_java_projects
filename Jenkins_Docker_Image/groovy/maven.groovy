import hudson.tasks.Maven
import hudson.tasks.Maven.MavenInstallation;
import hudson.tools.InstallSourceProperty;
import hudson.tools.ToolProperty;
import hudson.tools.ToolPropertyDescriptor
import hudson.tools.ZipExtractionInstaller;
import hudson.util.DescribableList
import jenkins.model.Jenkins;

def extensions = Jenkins.instance.getExtensionList(Maven.DescriptorImpl.class)[0]

List<MavenInstallation> installations = []
mavenToool = ['name': 'Maven3.3.9', 'home': '/usr/share/maven/']

println("Setting up tool: ${mavenToool.name} ")

installations.add(new MavenInstallation(mavenToool.name as String, mavenToool.home as String))

extensions.setInstallations(installations.toArray(new MavenInstallation[installations.size()]))
extensions.save()