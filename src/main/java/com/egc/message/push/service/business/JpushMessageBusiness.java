package com.egc.message.push.service.business;

import com.egc.message.push.service.enumeration.AudienceEnum;
import com.egc.message.push.service.enumeration.PlatformEnum;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.util.JpushMessageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JpushMessageBusiness {


    public StateCode sendToAndroidAndIos(PlatformEnum platformEnum, String msgContent, String title, String contentType, Map<String, String> extras) {
        return JpushMessageUtil.sendToAndroidAndIos(platformEnum, msgContent, title, contentType, extras);
    }

    public StateCode sendToAndroidAndIosAllAudience(AudienceEnum audienceEnum, List<String> audienceList, String msgContent, String title, String contentType, Map<String, String> extras) {
        return JpushMessageUtil.sendToAndroidAndIosAllAudience(audienceEnum, audienceList, msgContent, title, contentType, extras);
    }

}
