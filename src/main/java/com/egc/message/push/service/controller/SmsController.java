package com.egc.message.push.service.controller;

import com.egc.message.push.service.business.SmsBusiness;
import com.egc.message.push.service.config.BaseProperties;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.util.CheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Api(tags = {"短信接口"})
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsBusiness smsBusiness;

    @ApiOperation(value = "短信发送")
    @PostMapping("/tls")
    public StateCode sendSms(@ApiParam(value = "访问key") @RequestHeader(name = "accesskey", required = true) String accesskey,
                             @ApiParam(value = "手机号", required = true) @RequestParam(name = "phoneNumbers", required = true) String phoneNumbers,
                             @ApiParam(value = "短信签名", required = true) @RequestParam(name = "signName", required = true) String signName,
                             @ApiParam(value = "短信模板", required = true) @RequestParam(name = "templateCode", required = true) String templateCode,
                             @ApiParam(value = "短信模板变量", required = false) @RequestParam(name = "templateParam", required = false) String templateParam,
                             @ApiParam(value = "扩展字段", required = false) @RequestParam(name = "outId", required = false) String outId) {


        if (!CheckUtil.isSixteenCharacter(accesskey) || !BaseProperties.baseAckey.equals(accesskey)) {
            return new StateCode("-1", "非法访问key");
        }

        if (StringUtils.isEmpty(phoneNumbers)) {
            return new StateCode("-3", "手机号不能为空");
        }

        List<String> phoneNumbersList = Arrays.asList(phoneNumbers.split(","));

        for (String phone : phoneNumbersList) {
            if (!CheckUtil.isMobile(phone)) {
                return new StateCode("-3", "非法手机号");
            }
        }


        if (!CheckUtil.isJSONObject(templateParam)) {
            return new StateCode("-3", "JSON参数不合法");
        }

        return smsBusiness.sendSms(phoneNumbersList, signName, templateCode, templateParam, outId);
    }


}
