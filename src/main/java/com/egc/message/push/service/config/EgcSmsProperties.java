package com.egc.message.push.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EgcSmsProperties {

    public static String smsAccessKeyId;
    public static String smsAccessKeySecret;

    @Value("${egc.sms.accessKeyId}")
    public void setSmsAccessKeyId(String accessKeyId) {
        smsAccessKeyId = accessKeyId;
    }

    @Value("${egc.sms.accessKeySecret}")
    public void setSmsAccessKeySecret(String accessKeySecret) {
        smsAccessKeySecret = accessKeySecret;
    }
}
