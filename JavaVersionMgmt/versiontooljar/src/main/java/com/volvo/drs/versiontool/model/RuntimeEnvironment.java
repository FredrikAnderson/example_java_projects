package com.volvo.drs.versiontool.model;

public interface RuntimeEnvironment {
    
    enum RuntimeProduct {
        JBOSS,
        WEBSPHERE
    };
    
    enum RuntimeStage {
        DEV,
        TEST,
        QA,
        PROD
    };
    
    public boolean supportsRestVersionService();
    
    public RuntimeProduct getRuntimeProduct();

    public RuntimeStage getStage();

    public String getName();
    
}
