package com.egc.message.push.service.util;

import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {


    /**
     * 判断是否是手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 判断是否是数字与字母16位字符
     *
     * @param str
     * @return
     */
    public static boolean isSixteenCharacter(String str) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是后缀验证（jpg、png）
     *
     * @param url
     * @return
     */
    public static boolean isJpgPng(String url) {
        String regex = "^(http|https):\\.*?\\/.*?\\.(jpg|png)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);
        return m.matches();
    }



    /**
     * 判断是否是1~3数字
     *
     * @param str
     * @return
     */
    public static boolean isOneTwoThree(String str) {
        String regex = "^[1-3]$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 判断是否是JSONObject数据
     *
     * @param str
     * @return
     */
    public static boolean isJSONObject(String str) {
        try {
            JSONObject jsonStr = JSONObject.fromObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 判断是否是JSONArray数据
     *
     * @param str
     * @return
     */
    public static boolean isJSONArray(String str) {
        try {
            JSONArray jsonArray = JSONArray.fromObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    /**
     * 判断是否是符合平台标准
     * @param str
     * @return
     */
    public static boolean isPlatform(String str) {
        String regex = "^(^[A][L][L]$)|(^[A][N][D][R][O][I][D]$)|^([I][O][S]$)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 判断是否是符合目标标准
     * @param str
     * @return
     */
    public static boolean isTarget(String str) {
        String regex = "^(^[T][A][G]$)|(^[A][L][I][A][S]$)|^([R][E][G][I][S][T][R][A][T][I][O][N][I][D]$)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }




}
