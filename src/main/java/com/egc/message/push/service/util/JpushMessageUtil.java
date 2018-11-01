package com.egc.message.push.service.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.egc.message.push.service.config.EgcJpushProperties;
import com.egc.message.push.service.enumeration.AudienceEnum;
import com.egc.message.push.service.enumeration.PlatformEnum;
import com.egc.message.push.service.result.StateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class JpushMessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(JpushMessageUtil.class);


    private static JPushClient jPushClient = new JPushClient(EgcJpushProperties.JpushMasterSecret, EgcJpushProperties.JpushAppKey);


    public static StateCode sendToAndroidAndIos(PlatformEnum platformEnum, String msgContent, String title, String contentType, Map<String, String> extras){

        try {
            //构建PushPayload对象
            PushPayload pushPayload = JpushMessageUtil.buildPushObjectAndroidAndIos(platformEnum, msgContent, title, contentType, extras);
            logger.info("Message PushPayload :" + pushPayload.toString());
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("Message PushResult :" + pushResult.toString());
            if (pushResult.getResponseCode() == 200) {
               return new StateCode("0", "推送成功");
            }
        } catch (APIConnectionException e) {
            logger.info("连接错误", e);
            return new StateCode("-2", "连接错误");
        } catch (APIRequestException e) {
            logger.info("JPush服务器的错误响应。", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            return new StateCode("-1", "推送失败："+e.getErrorMessage());
        }
        return new StateCode();
    }


    public static StateCode sendToAndroidAndIosAllAudience(AudienceEnum audienceEnum, List<String> audienceList, String msgContent, String title, String contentType, Map<String, String> extras){

        try {

            PushPayload pushPayload = JpushMessageUtil.buildPushObjectAllAudience(audienceEnum, audienceList, msgContent, title, contentType, extras);
            logger.info("Message PushPayload :" + pushPayload.toString());
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("Message PushResult :" + pushResult.toString());
            if (pushResult.getResponseCode() == 200) {
                return new StateCode("0", "推送成功");
            }
        } catch (APIConnectionException e) {
            logger.info("连接错误", e);
            return new StateCode("-2", "连接错误");
        } catch (APIRequestException e) {
            logger.info("JPush服务器的错误响应。", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            return new StateCode("-1", "推送失败："+e.getErrorMessage());
        }
        return new StateCode();
    }



    public static PushPayload buildPushObjectAndroidAndIos(PlatformEnum platformEnum, String msgContent, String title, String contentType, Map<String, String> extras) {


        PushPayload.Builder pushPayload = PushPayload.newBuilder();
        pushPayload.setAudience(Audience.all());

        switch (platformEnum) {
            case ALL:
                pushPayload.setPlatform(Platform.android_ios());
                break;
            case IOS:
                pushPayload.setPlatform(Platform.ios());

                break;
            case ANDROID:
                pushPayload.setPlatform(Platform.android());
                break;
            default:
                break;

        }

        Message message = buildMessage(msgContent, title, contentType, extras);
        pushPayload.setMessage(message);

        Options options = buildOptions(null, null, null);
        pushPayload.setOptions(options);
        return pushPayload.build();
    }


    public static PushPayload buildPushObjectAllAudience(AudienceEnum audienceEnum, List<String> audienceList, String msgContent, String title, String contentType, Map<String, String> extras) {


        Message message = buildMessage(msgContent, title, contentType, extras);
        Options options = buildOptions(null, null, null);

        PushPayload.Builder pushPayload = PushPayload.newBuilder();
        pushPayload.setPlatform(Platform.android_ios());
        pushPayload.setAudience(Audience.all());
        pushPayload.setMessage(message);
        pushPayload.setOptions(options);


        switch (audienceEnum) {
            case TAG:
                pushPayload.setAudience(Audience.tag(audienceList));
                break;
            case ALIAS:
                pushPayload.setAudience(Audience.alias(audienceList));
                break;
            case REGISTRATIONID:
                pushPayload.setAudience(Audience.registrationId(audienceList));
                break;
            default:
                break;

        }

        pushPayload.setOptions(options);
        return pushPayload.build();
    }


    /**
     * 构建Message对象
     *
     * @param msgContent
     * @param title
     * @param contentType
     * @param extras
     * @return
     */
    public static Message buildMessage(String msgContent, String title, String contentType, Map<String, String> extras) {
        Message.Builder message = Message.newBuilder();
        //消息内容本身
        message.setMsgContent(msgContent);

        if (!StringUtils.isEmpty(title)) {
            //消息标题
            message.setTitle(title);
        }
        if (!StringUtils.isEmpty(contentType)) {
            //消息内容类型
            message.setContentType(contentType);
        }
        if (null != extras) {
            //JSON 格式的可选参数
            message.addExtras(extras);
        }

        return message.build();
    }


    /**
     * 构建Options对象
     *
     * @param sendno
     * @param overrideMsgId
     * @param bigPushDuration
     * @return
     */
    public static Options buildOptions(Integer sendno, Integer overrideMsgId, Integer bigPushDuration) {

        Options.Builder options = Options.newBuilder();
        if (!StringUtils.isEmpty(sendno)) {
            //推送序号
            options.setSendno(sendno);
        }
        if (!StringUtils.isEmpty(overrideMsgId)) {
            //要覆盖的消息 ID
            options.setOverrideMsgId(overrideMsgId);
        }
        if (!StringUtils.isEmpty(bigPushDuration)) {
            //定速推送时长(分钟)
            options.setBigPushDuration(bigPushDuration);
        }
        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
        options.setApnsProduction(false);
        return options.build();
    }




}