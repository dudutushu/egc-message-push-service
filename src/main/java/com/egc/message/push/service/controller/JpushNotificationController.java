package com.egc.message.push.service.controller;

import com.egc.message.push.service.business.JpushNotificationBusiness;
import com.egc.message.push.service.config.BaseProperties;
import com.egc.message.push.service.enumeration.AudienceEnum;
import com.egc.message.push.service.enumeration.PlatformEnum;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.util.CheckUtil;
import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"通知消息推送接口"})
@RestController
@RequestMapping("/notification")
public class JpushNotificationController {

    @Autowired
    JpushNotificationBusiness jpushNotificationBusiness;

    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accesskey", dataType = "String", required = true, value = "访问key"),
            @ApiImplicitParam(name = "platform", dataType = "String", required = false, value = "推送平台"),
            @ApiImplicitParam(name = "body", dataType = "String", required = true, value = "通知内容"),
            @ApiImplicitParam(name = "title", dataType = "String", required = true, value = "通知标题"),
            @ApiImplicitParam(name = "customStyleId", dataType = "integer", required = false, value = "通知栏样式ID"),
            @ApiImplicitParam(name = "extDataType", dataType = "integer", required = false, value = "通知栏样式类型"),
            @ApiImplicitParam(name = "extDataBody", dataType = "String", required = false, value = "大文本、文本条目、大图片"),
            @ApiImplicitParam(name = "extParameters", required = false, value = "扩展字段")
    })
    @ApiOperation(value = "推送平台")
    @PostMapping("/platform")
    public StateCode sendToAndroidAndIos(
            @RequestHeader(name = "accesskey", required = true) String accesskey,
            @RequestParam(name = "platform", required = false) String platform,
            @RequestParam(name = "body", required = true) String body,
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "customStyleId", required = false) Integer customStyleId,
            @RequestParam(name = "extDataType", required = false) Integer extDataType,
            @RequestParam(name = "extDataBody", required = false) String extDataBody,
            @RequestParam(name = "extParameters", required = false) String extParameters) {

        if (!CheckUtil.isSixteenCharacter(accesskey) || !BaseProperties.baseAckey.equals(accesskey)) {
            return new StateCode("-1", "非法访问key");
        }


        if(StringUtils.isEmpty(platform)){
            platform = "ALL"; //默认值
        }

        if (!CheckUtil.isPlatform(platform)) {
            return new StateCode("-3", "platform参数不合法");
        }

        if (StringUtils.isEmpty(body)) {
            return new StateCode("-3", "body参数不合法");
        }

        if (StringUtils.isEmpty(title)) {
            return new StateCode("-3", "title参数不合法");
        }

        if (null != extDataType) {

            if (!CheckUtil.isOneTwoThree(extDataType.toString())) {
                return new StateCode("-3", "style参数不合法");
            }

            if (StringUtils.isEmpty(extDataBody)) {
                return new StateCode("-3", "styleBody参数不合法");
            }

            switch (extDataType) {
                case 2:
                    if (!CheckUtil.isJSONObject(extDataBody)) {
                        return new StateCode("-3", "styleContent参数不合法");
                    }
                    break;
                case 3:
                    if (!CheckUtil.isJpgPng(extDataBody)) {
                        return new StateCode("-3", "styleContent参数不合法");
                    }
                    break;
                default:
                    break;
            }
        }

        Map<String, String> extras = null;

        if (!StringUtils.isEmpty(extParameters)) {

            if (!CheckUtil.isJSONObject(extParameters)) {
                return new StateCode("-3", "extParameters参数不合法");
            }

            extras = JSONObject.fromObject(extParameters);
        }


        PlatformEnum platformEnum = PlatformEnum.valueOf(platform);

        return jpushNotificationBusiness.sendToAndroidAndIos(platformEnum, body, title, customStyleId, extDataType, extDataBody, extras);
    }


    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accesskey", dataType = "String", required = true, value = "访问key"),
            @ApiImplicitParam(name = "target", dataType = "String", required = true, value = "推送目标类型"),
            @ApiImplicitParam(name = "targetValue", dataType = "String", required = true, value = "推送目标"),
            @ApiImplicitParam(name = "body", dataType = "String", required = true, value = "通知内容"),
            @ApiImplicitParam(name = "title", dataType = "String", required = true, value = "通知标题"),
            @ApiImplicitParam(name = "customStyleId", dataType = "integer", required = false, value = "通知栏样式ID"),
            @ApiImplicitParam(name = "extDataType", dataType = "integer", required = false, value = "通知栏样式类型"),
            @ApiImplicitParam(name = "extDataBody", dataType = "String", required = false, value = "大文本、文本条目、大图片"),
            @ApiImplicitParam(name = "extParameters", required = false, value = "扩展字段")
    }
    )
    @ApiOperation(value = "推送目标")
    @PostMapping("/target")
    public StateCode sendToAndroidAndIosAllAudience(
            @RequestHeader(name = "accesskey", required = true) String accesskey,
            @RequestParam(name = "target", required = true) String target,
            @RequestParam(value = "targetValue", required = true) String targetValue,
            @RequestParam(name = "body", required = true) String body,
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "customStyleId", required = false) Integer customStyleId,
            @RequestParam(name = "extDataType", required = false) Integer extDataType,
            @RequestParam(name = "extDataBody", required = false) String extDataBody,
            @RequestParam(name = "extParameters", required = false) String extParameters) {


        if (!CheckUtil.isSixteenCharacter(accesskey) || !BaseProperties.baseAckey.equals(accesskey)) {
            return new StateCode("-1", "非法访问key");
        }

        if (StringUtils.isEmpty(target) || !CheckUtil.isTarget(target)) {
            return new StateCode("-3", "target参数不合法");
        }


        if (!CheckUtil.isJSONArray(targetValue)) {
            return new StateCode("-3", "targetValue参数不合法");
        }

        if (null != extDataType) {

            if (!CheckUtil.isOneTwoThree(extDataType.toString())) {
                return new StateCode("-3", "style参数不合法");
            }

            if (StringUtils.isEmpty(extDataBody)) {
                return new StateCode("-3", "styleBody参数不合法");
            }

            switch (extDataType) {
                case 2:
                    if (!CheckUtil.isJSONObject(extDataBody)) {
                        return new StateCode("-3", "styleContent参数不合法");
                    }
                    break;
                case 3:
                    if (!CheckUtil.isJpgPng(extDataBody)) {
                        return new StateCode("-3", "styleContent参数不合法");
                    }
                    break;
                default:
                    break;
            }

        }

        Map<String, String> extras = null;

        if (!StringUtils.isEmpty(extParameters)) {

            if (!CheckUtil.isJSONObject(extParameters)) {
                return new StateCode("-3", "extParameters参数不合法");
            }

            extras = JSONObject.fromObject(extParameters);
        }

        AudienceEnum audienceEnum = AudienceEnum.valueOf(target);
        JSONArray jsonArray = JSONArray.fromObject(targetValue);
        List<String> audienceLists = (List<String>) JSONArray.toCollection(jsonArray, String.class);
        return jpushNotificationBusiness.sendToAndroidAndIosAllAudience(audienceEnum, audienceLists, body, title, customStyleId, extDataType, extDataBody, extras);
    }


}
