package com.shou.crabscore.common.util.netease;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.JasyptUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 发送验证码
 * 接口示例：https://dev.yunxin.163.com/docs/product/%E7%9F%AD%E4%BF%A1/%E7%9F%AD%E4%BF%A1%E6%8E%A5%E5%85%A5%E7%A4%BA%E4%BE%8B
 * 接口概述：https://dev.yunxin.163.com/docs/product/IM%E5%8D%B3%E6%97%B6%E9%80%9A%E8%AE%AF/%E6%9C%8D%E5%8A%A1%E7%AB%AFAPI%E6%96%87%E6%A1%A3/%E6%8E%A5%E5%8F%A3%E6%A6%82%E8%BF%B0
 *
 * @author netease liuxuanlin
 */
@Log4j2
public class MessageUtil {
    /**
     * 校验验证码的请求路径URL
     */
    private static final String VERIFY_SEVER_URL = "https://api.netease.im/sms/verifycode.action";
    /**
     * 发送验证码的请求路径URL
     */
    private static final String SEND_SERVER_URL = "https://api.netease.im/sms/sendcode.action";
    /**
     * 网易云信分配的账号，请替换你在管理后台应用下申请的App key
     */
    private static final String APP_KEY = JasyptUtil.decryptPwd("spencercjh", "ccZmePF72Dr734aVSp8l65r6A7g1obf1RhP2N3YuiFu/llRcG/uuP4TSc/GQaFI0");
    /**
     * 网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
     */
    private static final String APP_SECRET = JasyptUtil.decryptPwd("spencercjh", "R5TJFbyweFBh6EVozpDKVMNDLKy8EuAn");
    /**
     * 随机数
     */
    private static final String NONCE = String.valueOf(new Random().nextInt(128));
    /**
     * 短信模板ID
     */
    private static final String TEMPLATE_ID = "9404119";
    /**
     * 手机号
     */
    private static final String MOBILE = "15000131965";
    /**
     * 验证码长度，范围4～10，默认为4
     */
    private static final String CODE_LENGTH = "4";

    @SuppressWarnings("SpellCheckingInspection")
    public static boolean sendCode(String mobile) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SEND_SERVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("templateid", TEMPLATE_ID));
        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
        nameValuePairs.add(new BasicNameValuePair("codeLen", CODE_LENGTH));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity(), "utf-8").contains(CommonConstant.SUCCESS.toString());
    }

    public static boolean verifyCode(String mobile, String code) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(VERIFY_SEVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("code", code));
        nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity(), "utf-8").contains(CommonConstant.SUCCESS.toString());
    }

    public static void main(String[] args) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(SEND_SERVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
        // 设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nameValuePairs.add(new BasicNameValuePair("templateid", TEMPLATE_ID));
        nameValuePairs.add(new BasicNameValuePair("mobile", MOBILE));
        nameValuePairs.add(new BasicNameValuePair("codeLen", CODE_LENGTH));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
        log.info("结果" + EntityUtils.toString(response.getEntity(), "utf-8"));
    }
}