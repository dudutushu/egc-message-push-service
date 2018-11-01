package com.egc.message.push.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EgcJpushProperties {

    public static String JpushAppKey;
    public static String JpushMasterSecret;

    @Value("${egc.jpush.appKey}")
    public void setJpushAppKey(String appKey) {
        JpushAppKey = appKey;
    }

    @Value("${egc.jpush.masterSecret}")
    public void setJpushMasterSecret(String masterSecret) {
        JpushMasterSecret = masterSecret;
    }

}
