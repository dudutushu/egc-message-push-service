package com.egc.message.push.service.business;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.egc.message.push.service.result.StateCode;
import com.egc.message.push.service.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsBusiness {

    private static final Logger logger = LoggerFactory.getLogger(SmsBusiness.class);


    public StateCode sendSms(List<String> phoneNumbersList, String signName, String templateCode, String templateParam, String outId) {
        try {

            String message = "";

            for (int i = 0; i < phoneNumbersList.size() ; i++) {
                String phoneNumber = phoneNumbersList.get(i);

                SendSmsResponse response = SmsUtil.sendSms(phoneNumber, signName, templateCode, templateParam, outId);

                if ("OK".equals(response.getCode())) {
                    message += phoneNumber + "：发送成功";
                } else {
                    message += phoneNumber + "：" + response.getMessage();
                }

                //逗号拼接
                if(i != phoneNumbersList.size() -1){
                    message += ",";
                }
            }

            return new StateCode("0", message);
        } catch (ClientException e) {
            logger.info("短信发送异常： " + e);
            return new StateCode("-2", "发送异常");
        }

    }


}
