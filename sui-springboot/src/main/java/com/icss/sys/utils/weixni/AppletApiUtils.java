package com.icss.sys.utils.weixni;

import com.icss.sys.base.constant.AppletConst;
import com.icss.sys.utils.http.HttpClientUtils;
import com.icss.sys.utils.jwt.JwtUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.icss.sys.base.module.config.service.SysConfigService.getSysConfig;

/**
 * 小程序工具类
 */
public class AppletApiUtils {
    //发送手机APP信息
    public static void sendAppMessage(String title,String content,String groupName){
        content = content.replace("\n", "").replace("\r", "").replace(" ","");
        JSONObject result = HttpClientUtils.httpGet("https://api.day.app/opfBDHa4jiu6yjN4BWZUEb/"+title+"/"+content+"?sound=calypso&groupName="+groupName);
        System.out.println(result);
    }
    public static String getJscode2session(String code,String from){
        final String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=GRANT_TYPE";
        String reqUrl = url.replace("APPID", getSysConfig(AppletConst.APPLET_APPID)).replace("SECRET", getSysConfig(AppletConst.APPLET_APPSECRET)).replace("JSCODE", code).replace("GRANT_TYPE", "authorization_code");
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        Integer errcode = (Integer) result.get("errcode");
        Map<String,String> map = new HashMap<>(); //token设置值
        if (errcode == null) {
            String unionid = (String) result.get("unionid");
            String openid = (String) result.get("openid");
            String sessionKey = (String) result.get("session_key");
            map.put("unionid",unionid);
            map.put("openid",openid);
            map.put("sessionKey",sessionKey);
            map.put("from",from);
        }
        String token = JwtUtils.generateToken(map);
        return token;
    }
    public static JSONObject getAccessToken(){
        final String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String reqUrl = url.replace("APPID", getSysConfig(AppletConst.APPLET_APPID)).replace("APPSECRET", getSysConfig(AppletConst.APPLET_APPSECRET));
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        return result;
    }
    public static JSONObject subscribeSend(String accessToken,JSONObject param){
        final String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=ACCESS_TOKEN";
        String reqUrl = url.replace("ACCESS_TOKEN", accessToken);
        JSONObject result = HttpClientUtils.httpPost(reqUrl,param);
        return result;
    }



}
