package com.egc.message.push.service.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagListResult;
import com.egc.message.push.service.config.EgcJpushProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JpushTagUtil {

    private static final Logger logger = LoggerFactory.getLogger(JpushTagUtil.class);

    private static JPushClient jPushClient = new JPushClient(EgcJpushProperties.JpushMasterSecret, EgcJpushProperties.JpushAppKey);

    public static List<String> getTagList() throws APIConnectionException, APIRequestException {
        TagListResult tagListResult = jPushClient.getTagList();
        return tagListResult.tags;
    }


}
