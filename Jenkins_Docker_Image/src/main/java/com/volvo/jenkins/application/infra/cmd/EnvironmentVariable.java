package com.volvo.jenkins.application.infra.cmd;

public class EnvironmentVariable implements DeploymentConfig {

    private String name;
    
    private String value;
    
    String cmd = "oc set env dc/%s %s=%s";
    
    public EnvironmentVariable(String name, String value) {
        this.name   = name.toUpperCase();
        this.value  = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getCommand(String stage) {
        String replacedStages = value.replace("_stage_", stage);
        if (replacedStages.contains(" ")) {
            replacedStages = getStartEndArgumentTag() + replacedStages + getStartEndArgumentTag();
        }
        return String.format(cmd, stage, name, replacedStages);            
    }

    @Override
    public String toString() {
        return "EnvironmentVariable [name=" + name + ", value=" + value + "]";
    }    
    
    String getStartEndArgumentTag() {
        String toret = "\"";
        // If not Windows we are assuming Linux
        if (!System.getProperty("os.name").startsWith("Windows")) {
            toret = "'";
        }        
        return toret;
    }
    
}
