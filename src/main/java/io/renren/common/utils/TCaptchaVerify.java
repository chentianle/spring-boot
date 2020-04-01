package io.renren.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.TreeMap;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.captcha.v20190722.CaptchaClient;

import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;

public class TCaptchaVerify {
    private static final String APP_ID = "2073845417";
    private static final String APP_SECRET = "0B_tjxl04kEkJeRYskn4rEA**";
//    private static final String VERIFY_URI = "https://captcha.tencentcloudapi.com?aid=%s&AppSecretKey=%s&Ticket=%s&Randstr=%s&UserIP=%s";
    private static final String VERIFY_URI = "https://captcha.tencentcloudapi.com?Action=DescribeCaptchaResult&Version=2019-07-22&CaptchaType=9&CaptchaAppId=%s&AppSecretKey=%s&Ticket=%s&Randstr=%s&UserIp=%s";

    public static int verifyTicket(String ticket, String rand, String userIp) {

        try{

            Credential cred = new Credential("AKIDwDF1XrGkJFzThfNvAnANK1U6TZP0aYT9", "dIhphYMtBCLErEKCoAHGOGwNltlNisnS");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("captcha.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CaptchaClient client = new CaptchaClient(cred, "", clientProfile);

            String params = "{\"CaptchaType\":9,\"Ticket\":"+ticket+",\"UserIp\":"+userIp+",\"Randstr\":"+rand+",\"CaptchaAppId\":"+APP_ID+",\"AppSecretKey\":"+APP_SECRET+"}";
            DescribeCaptchaResultRequest req = DescribeCaptchaResultRequest.fromJsonString(params, DescribeCaptchaResultRequest.class);

            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);

            String res = DescribeCaptchaResultRequest.toJsonString(resp);
            System.out.println(res); // 临时输出

            JSONObject result = JSON.parseObject(res);
            // 返回码
            int code = result.getInteger("CaptchaCode");
            // 恶意等级
            int evilLevel = result.getInteger("EvilLevel");

            // 验证成功
            if (code == 1) return evilLevel;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return -1;
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpPost httpPost;
//        CloseableHttpResponse response = null;
//        try {
//            String a = "{\"CaptchaType\": 9,\"Ticket\":"+ticket+",\"UserIp\":"+userIp+",\"Randstr\":"+rand+",\"CaptchaAppId\":"+APP_ID+",\"AppSecretKey\":"+APP_SECRET+"}";
//            TreeMap<String, String> captchaAuthorization = TencentCloudAPITC3.createCaptchaAuthorization(a);
//            httpPost = new HttpPost(String.format(VERIFY_URI,
//                    APP_ID,
//                    APP_SECRET,
//                    URLEncoder.encode(ticket, "UTF-8"),
//                    URLEncoder.encode(rand, "UTF-8"),
//                    URLEncoder.encode(userIp, "UTF-8")
//            ));
//            Iterator<String> iterator = captchaAuthorization.keySet().iterator();
//            while (iterator.hasNext()){
//                String key = iterator.next();
//                httpPost.setHeader(key,captchaAuthorization.get(key));
//            }
//            response = httpclient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                String res = EntityUtils.toString(entity);
//                System.out.println(res); // 临时输出
//
//                JSONObject result = JSON.parseObject(res);
//                // 返回码
//                int code = result.getInteger("response");
//                // 恶意等级
//                int evilLevel = result.getInteger("evil_level");
//
//                // 验证成功
//                if (code == 1) return evilLevel;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                response.close();
//            } catch (Exception ignore) {
//            }
//        }
//
//        return -1;
    }
}
