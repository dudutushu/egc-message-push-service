package com.egc.message.push.service.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.egc.message.push.service.config.EgcJpushProperties;
import com.egc.message.push.service.enumeration.AudienceEnum;
import com.egc.message.push.service.enumeration.PlatformEnum;
import com.egc.message.push.service.result.StateCode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


public class JpushNotificationUtil {

    private static final Logger logger = LoggerFactory.getLogger(JpushNotificationUtil.class);

    private static JPushClient jPushClient = new JPushClient(EgcJpushProperties.JpushMasterSecret, EgcJpushProperties.JpushAppKey);

    /**
     * 平台通知推送
     *
     * @param alert
     * @param title
     * @param builderId
     * @param style
     * @param styleContent
     * @param extras
     * @return
     */
    public static StateCode sendToAndroidAndIos(PlatformEnum platformEnum, String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras) {

        try {
            //构建PushPayload对象
            PushPayload pushPayload = JpushNotificationUtil.buildPushObjectAndroidAndIos(platformEnum, alert, title, builderId, style, styleContent, extras);
            logger.info("Notification PushPayload :" + pushPayload.toString());
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("Notification PushResult :" + pushResult.toString());
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
            return new StateCode("-1", "推送失败：" + e.getErrorMessage());
        }
        return new StateCode();
    }


    /**
     * 根据目标通知推送
     *
     * @param alert
     * @param title
     * @param builderId
     * @param style
     * @param styleContent
     * @param extras
     * @return
     */
    public static StateCode sendToAndroidAndIosAllAudience(AudienceEnum audienceEnum, List<String> audienceList, String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras) {

        try {

            PushPayload pushPayload = JpushNotificationUtil.buildPushObjectAllAudience(audienceEnum, audienceList, alert, title, builderId, style, styleContent, extras);
            logger.info("Notification PushPayload :" + pushPayload.toString());
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("Notification PushResult :" + pushResult.toString());
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
            return new StateCode("-1", "推送失败：" + e.getErrorMessage());
        }
        return new StateCode();
    }



    public static PushPayload buildPushObjectAndroidAndIos(PlatformEnum platformEnum, String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras) {

        PlatformNotification androidNotification = null;
        PlatformNotification iosNotification = null;

        PushPayload.Builder pushPayload = PushPayload.newBuilder();
        pushPayload.setAudience(Audience.all());

        switch (platformEnum) {
            case ALL:
                pushPayload.setPlatform(Platform.android_ios());
                androidNotification = builderAndroidNotification(alert, title, builderId, style, styleContent, extras);
                iosNotification = buildIosNotification(alert, extras);
                pushPayload.setNotification(Notification.newBuilder()
                        .addPlatformNotification(androidNotification)
                        .addPlatformNotification(iosNotification)
                        .build());
                break;
            case IOS:
                pushPayload.setPlatform(Platform.ios());

                iosNotification = buildIosNotification(alert, extras);
                pushPayload.setNotification(Notification.newBuilder()
                        .addPlatformNotification(iosNotification)
                        .build());
                break;
            case ANDROID:
                pushPayload.setPlatform(Platform.android());

                androidNotification = builderAndroidNotification(alert, title, builderId, style, styleContent, extras);
                pushPayload.setNotification(Notification.newBuilder()
                        .addPlatformNotification(androidNotification)
                        .build());
                break;
            default:
                break;

        }

        Options options = buildOptions(null, null, null);
        pushPayload.setOptions(options);
        return pushPayload.build();
    }


    public static PushPayload buildPushObjectAllAudience(AudienceEnum audienceEnum, List<String> audienceList, String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras) {


        PlatformNotification androidNotification = builderAndroidNotification(alert, title, builderId, style, styleContent, extras);
        PlatformNotification iosNotification = buildIosNotification(alert, extras);
        Options options = buildOptions(null, null, null);


        PushPayload.Builder pushPayload = PushPayload.newBuilder();
        pushPayload.setPlatform(Platform.android_ios());
        pushPayload.setAudience(Audience.all());

        pushPayload.setNotification(Notification.newBuilder()
                .addPlatformNotification(androidNotification)
                .addPlatformNotification(iosNotification)
                .build());
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
     * 构建AndroidNotification对象
     *
     * @param alert
     * @param title
     * @param builderId
     * @param style
     * @param styleContent
     * @param extras
     * @return
     */
    public static PlatformNotification builderAndroidNotification(String alert, String title, Integer builderId, Integer style, String styleContent, Map<String, String> extras) {

        AndroidNotification.Builder androidNotification = AndroidNotification.newBuilder();
        //通知内容
        androidNotification.setAlert(alert);
        //通知标题
        androidNotification.setTitle(title);
        if (!StringUtils.isEmpty(builderId)) {
            //通知栏样式 ID
            androidNotification.setBuilderId(builderId);
        }

        if (null != style) {
            //通知栏样式类型
            androidNotification.setStyle(style);
            switch (style) {
                case 1:
                    //大文本通知栏样式
                    androidNotification.setBigText(styleContent);
                    break;
                case 2:
                    //文本条目通知栏样式
                    JsonObject inbox = new JsonParser().parse(styleContent).getAsJsonObject();
                    androidNotification.setInbox(inbox);
                    break;
                case 3:
                    //大图片通知栏样式
                    androidNotification.setBigPicPath(styleContent);
                    break;
                default:
                    break;
            }
        }

        //扩展字段
        androidNotification.addExtras(extras);
        return androidNotification.build();
    }


    /**
     * 构建IosNotification对象
     *
     * @param alert
     * @param extras
     * @return
     */
    public static PlatformNotification buildIosNotification(String alert, Map<String, String> extras) {
        IosNotification.Builder iosNotification = IosNotification.newBuilder();
        //通知内容
        iosNotification.setAlert(alert);
        //通知提示声音
        iosNotification.setSound("sound.caf");
        //应用角标
        iosNotification.incrBadge(1);
        //扩展字段
        iosNotification.addExtras(extras);
        return iosNotification.build();
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