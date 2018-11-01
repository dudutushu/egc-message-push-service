package com.egc.message.push.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseProperties {

    public static String baseAckey;

    @Value("${egc.base.ackey}")
    public void setAckey(String ackey) {
        baseAckey = ackey;
    }


}
