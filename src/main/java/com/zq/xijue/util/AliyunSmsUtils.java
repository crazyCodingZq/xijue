package com.zq.xijue.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: zhangQ
 * @Date: 2020/5/1 23:48
 */
public class AliyunSmsUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunSmsUtils.class);
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAIag7J844dMyTS";
    private static final String ACCESS_KEY_SECRET = "j2uOatd9aFv6jT7AwSg5iPj5yPmmT2";
    private static final String SIGN_NAME = "夕觉素材";
    private static final String TEMPLATE_CODE = "SMS_189521195";

    public static boolean sendSms(String phone, String code) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(SIGN_NAME);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(TEMPLATE_CODE);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            LOGGER.info("手机号：{} 短信发送成功", phone);
            return true;
        } else {
            LOGGER.info("手机号：{} 短信发送失败", phone);
        }
        return false;
    }

    /**
     * 创建四位数验证码
     *
     * @return java.lang.String
     */
    public static String getPhoneRandomCode() {
        int code = (int) ((Math.random() * 9999) + 100);
        return code + "";
    }
}
