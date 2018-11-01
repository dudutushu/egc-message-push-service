package com.egc.message.push.service.controller;

import com.egc.message.push.service.business.JpushMessageBusiness;
import com.egc.message.push.service.config.BaseProperties;
import com.egc.message.push.service.enumeration.AudienceEnum;
import com.egc.message.push.service.enumeration.PlatformEnum;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.util.CheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"自定义消息推送接口"})
@RestController
@RequestMapping("/message")
public class JpushMessageController {

    @Autowired
    JpushMessageBusiness jpushMessageBusiness;

    @ApiOperation(value = "推送平台")
    @PostMapping("/platform")
    public StateCode sendToAndroidAndIos(
            @ApiParam(value = "访问key") @RequestHeader(name = "accesskey", required = true) String accesskey,
            @ApiParam(value = "推送平台", required = false) @RequestParam(name = "platform", required = false) String platform,
            @ApiParam(value = "消息内容", required = true) @RequestParam(name = "body", required = true) String body,
            @ApiParam(value = "消息标题", required = true) @RequestParam(name = "title", required = true) String title,
            @ApiParam(value = "消息类型", required = false) @RequestParam(name = "contentType", required = false) String contentType,
            @ApiParam(value = "扩展字段", required = false) @RequestParam(name = "extParameters", required = false) String extParameters) {

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


        Map<String, String> extras = null;
        if (!StringUtils.isEmpty(extParameters)) {
            if (!CheckUtil.isJSONObject(extParameters)) {
                return new StateCode("-3", "extParameters参数不合法");
            }
            extras = JSONObject.fromObject(extParameters);
        }


        PlatformEnum platformEnum = PlatformEnum.valueOf(platform);

        return jpushMessageBusiness.sendToAndroidAndIos(platformEnum, body, title, contentType, extras);
    }

    @ApiOperation(value = "推送目标")
    @PostMapping("/target")
    public StateCode sendToAndroidAndIosAllAudience(
            @ApiParam(value = "访问key") @RequestHeader(name = "accesskey", required = true) String accesskey,
            @ApiParam(value = "推送目标类型", required = true) @RequestParam(name = "target", required = true) String target,
            @ApiParam(value = "推送目标", required = true) @RequestParam(name = "targetValue", required = true) String targetValue,
            @ApiParam(value = "消息内容", required = true) @RequestParam(name = "body", required = true) String body,
            @ApiParam(value = "消息标题", required = true) @RequestParam(name = "title", required =true) String title,
            @ApiParam(value = "消息类型", required = false) @RequestParam(name = "contentType", required = false) String contentType,
            @ApiParam(value = "扩展字段", required = false) @RequestParam(name = "extParameters", required = false) String extParameters) {


        if (!CheckUtil.isSixteenCharacter(accesskey) || !BaseProperties.baseAckey.equals(accesskey)) {
            return new StateCode("-1", "非法访问key");
        }

        if (StringUtils.isEmpty(target) || !CheckUtil.isTarget(target)) {
            return new StateCode("-3", "target参数不合法");
        }

        if (!CheckUtil.isJSONArray(targetValue)) {
            return new StateCode("-3", "targetValue参数不合法");
        }


        if (StringUtils.isEmpty(body)) {
            return new StateCode("-3", "body参数不合法");
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
        return jpushMessageBusiness.sendToAndroidAndIosAllAudience(audienceEnum, audienceLists, body, title, contentType, extras);
    }


}
