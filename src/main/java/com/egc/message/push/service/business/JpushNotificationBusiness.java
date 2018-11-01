package com.egc.message.push.service.business;

import com.egc.message.push.service.enumeration.AudienceEnum;
import com.egc.message.push.service.enumeration.PlatformEnum;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.util.JpushNotificationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class JpushNotificationBusiness {


    public StateCode sendToAndroidAndIos(PlatformEnum platformEnum, String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras) {
        return JpushNotificationUtil.sendToAndroidAndIos(platformEnum, alert, title, builderId, style, styleContent, extras);
    }


    public StateCode sendToAndroidAndIosAllAudience(AudienceEnum audienceEnum, List<String> audienceList, String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras){
        return JpushNotificationUtil.sendToAndroidAndIosAllAudience(audienceEnum, audienceList, alert, title, builderId, style, styleContent, extras);
    }

}
