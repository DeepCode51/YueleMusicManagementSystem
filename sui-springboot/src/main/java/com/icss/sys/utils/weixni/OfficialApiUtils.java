package com.icss.sys.utils.weixni;

import com.icss.sys.base.constant.AppletConst;
import com.icss.sys.base.entity.UserToken;
import com.icss.sys.utils.http.HttpClientUtils;
import com.icss.sys.utils.jwt.JwtUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.icss.sys.base.module.config.service.SysConfigService.getSysConfig;

/**
 * 公众号工具类
 */
public class OfficialApiUtils {
    //微信session信息获取
    public static String getJscode2session(String code,String from){//暂时没有用
        final String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=GRANT_TYPE";
        String reqUrl = url.replace("APPID", getSysConfig(AppletConst.APPLET_APPID)).replace("SECRET", getSysConfig(AppletConst.APPLET_APPSECRET)).replace("JSCODE", code).replace("GRANT_TYPE", "authorization_code");
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        Integer errcode = (Integer) result.get("errcode");
        Map<String,String> map = new HashMap<>(); //token设置值
        if (errcode == null) {
            String unionid = (String) result.get("unionid");
            String openid = (String) result.get("openid");
            map.put("unionid",unionid);
            map.put("openid",openid);
            map.put("from",from);
        }
        String token = JwtUtils.generateToken(map);
        return token;
    }
    //删除菜单
    public static JSONObject delMenu() {
        final String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
        String reqUrl = url.replace("ACCESS_TOKEN", "access_token");
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        return result;
    }
    //添加菜单
    public static JSONObject createMenu(JSONObject menuJson) {
        final String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        String reqUrl = url.replace("ACCESS_TOKEN", "access_token");
        JSONObject result = HttpClientUtils.httpPost(reqUrl,menuJson);
        return result;
    }

    //    //获取获取code重定向的地址
    public static String getOfficialCodeUrl(String toUrl) {
        final String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        String reqUrl = url.replace("APPID", getSysConfig("officialAppId"))
                .replace("SCOPE", "snsapi_userinfo")
                .replace("REDIRECT_URI", toUrl);
        return reqUrl;
    }

    //获取AccessToken和openid:该AccessToken是提供给获取用户头像基础信息用
    public static JSONObject getUserAccessToken(String code) {
        //        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        //        String reqUrl = url.replace("APPID", getSysConfig(OfficialConstants.OFFICIAL_APPID))
        //                .replace("SECRET", getSysConfig(OfficialConstants.OFFICIAL_APPSECRET))
        //                .replace("CODE", code);
        //        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        UserToken token = new UserToken();
        token.setUnionid("123456");
        token.setOpenid("789");
        token.setAccessToken("abc");
        JSONObject result = JSONObject.fromObject(token);
        return result;
    }

    //获取用户基础信息
    public static JSONObject getUserInfo(UserToken userToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String reqUrl = url.replace("ACCESS_TOKEN", userToken.getAccessToken())
                .replace("OPENID", userToken.getOpenid());
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        return result;
    }

    //获取测试AccessToken和openid
    public static JSONObject getTestUserAccessToken(String code) {
        JSONObject json = new JSONObject();
        json.put("openid", "test_openid_wx001");
        json.put("unionid", "o269x0fV7ynUO4-1zd3DRDJM3nEI");
        json.put("access_token", "test_access_token_001");
        return json;
    }

    //获取测试用户基础信息
    public static JSONObject getTestUserInfo(UserToken userToken) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("openid", userToken.getOpenid());
        userInfo.put("nickname", "test");
        userInfo.put("sex", "1");
        userInfo.put("city", "厦门");
        userInfo.put("province", "福建");
        userInfo.put("country", "中国");
        userInfo.put("headimgurl", getSysConfig("pic.default.user"));
        return userInfo;
    }

    //获取模板消息用的 accessToken
    public static JSONObject getAccessToken() {
        final String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String reqUrl = url.replace("APPID", getSysConfig("officialAppId").replace("APPSECRET", getSysConfig("officialAppSecret")));
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        return result;
    }

    //发送模板消息
    public static JSONObject templateSend(String accessToken, JSONObject param) {
        final String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String reqUrl = url.replace("ACCESS_TOKEN", accessToken);
        JSONObject result = HttpClientUtils.httpPost(reqUrl, param);
        return result;
    }

    //获取关注用户列表
    public static List<String> getUserList(String accessToken) {
        final String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
        String reqUrl = url.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPENID", "");
        JSONObject result = HttpClientUtils.httpGet(reqUrl);
        JSONArray jsonArray = result.getJSONObject("data").getJSONArray("openid");
        List<String> list = JSONArray.toList(jsonArray, String.class);// 过时方法
        return list;
    }


    //发送消息
//    public static JSONObject sendTemplateMessage(String openid, String tmplId, String access_token, JSONObject params) {
//        TemplateMessage message = new TemplateMessage();
//        message.setTouser(openid);
//        message.setTemplate_id(tmplId);
//        message.setUrl("http://suifeng.4kb.cn/admin/official/author/login");
//        message.setData(params);
//        JSONObject data = JSONObject.fromObject(message);
//        JSONObject result = OfficialApiUtils.templateSend(access_token, data);
//        return result;
//    }


    //群发推送
    public static JSONObject sendall(String accessToken) {
        final String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
        String reqUrl = url.replace("ACCESS_TOKEN", accessToken);
        String msg = "{'filter':{'is_to_all':true},'text':{'content':'CONTENT'},'msgtype':'text'}";
        JSONObject jsonObject = JSONObject.fromObject(msg);
        JSONObject result = HttpClientUtils.httpPost(reqUrl, jsonObject);
        return result;
    }



}
