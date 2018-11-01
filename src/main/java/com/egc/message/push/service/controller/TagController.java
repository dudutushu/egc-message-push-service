package com.egc.message.push.service.controller;

import com.egc.message.push.service.business.SmsBusiness;
import com.egc.message.push.service.business.TagBusiness;
import com.egc.message.push.service.config.BaseProperties;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.result.StateCodeData;
import com.egc.message.push.service.util.CheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(tags = {"标签接口"})
@RestController
public class TagController {

    @Autowired
    TagBusiness tagBusiness;

    @ApiOperation(value = "查询标签列表")
    @GetMapping("/tags")
    public StateCodeData getTagList(@ApiParam(value = "访问key") @RequestHeader(name = "accesskey", required = true) String accesskey) {

        if (!CheckUtil.isSixteenCharacter(accesskey) || !BaseProperties.baseAckey.equals(accesskey)) {
            return new StateCodeData("-1", "非法访问key",null);
        }

        return tagBusiness.getTagList();
    }


}
