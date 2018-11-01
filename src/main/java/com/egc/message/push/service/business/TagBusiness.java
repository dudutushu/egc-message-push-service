package com.egc.message.push.service.business;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.result.StateCodeData;
import com.egc.message.push.service.util.JpushTagUtil;
import com.egc.message.push.service.util.SmsUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagBusiness {

    private static final Logger logger = LoggerFactory.getLogger(TagBusiness.class);


    public StateCodeData getTagList() {
        try {
            List<String> tagList = JpushTagUtil.getTagList();

            //组装数据
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tags", tagList);

            return new StateCodeData("0","查询成功", jsonObject);
        } catch (APIConnectionException e) {
            logger.info("连接错误", e);
            return new StateCodeData("-2", "连接错误", null);
        } catch (APIRequestException e) {
            logger.info("JPush服务器的错误响应。", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            return new StateCodeData("-2", "查询失败：" + e.getErrorMessage(), null);
        }
    }



}
