package com.admin.web.controller.weixin;

import com.admin.web.model.weixin.WxMediaArticles;
import com.admin.web.util.R;
import com.admin.web.util.WxUtils;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.*;
import org.omg.CORBA.Object;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 微信工具类 controller
 */
@ControllerBind(controllerKey = "/weixin/config")
public class WxConfigController extends Controller {

    /**
     *  服务号授权跳转
     */
    public void oauth() {
        // 华人老板
        // String appId = PropKit.get("service.appid");
        // 途听
        String appId = PropKit.get("tut.appid");

        String redirectUri = null;
        String state = getPara("state","");
        try {
            System.out.println("oauth state = " + state);
            // 华人老板
            //redirectUri = URLEncoder.encode(PropKit.get("service.url") + "/wx/user/login", "UTF-8");
            // 途听
            redirectUri = URLEncoder.encode(PropKit.get("tut.url") + "/wx/user/login", "UTF-8");
            if (StrKit.notBlank(state)){
                state = URLEncoder.encode(state, "UTF-8");

                System.out.println("oauth state URLEncoder = " + state);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //String state = "http://www.baidu.com/openid=okid";

        String url = SnsAccessTokenApi.getAuthorizeURL(appId, redirectUri, state, false);
        renderJson(R.ok().put(url));
    }


    /**
     *
     * JS SDK 参数
     *
     */
    public void jsConfig() {

        JsTicket jsApiTicket = JsTicketApi.getTicket(JsTicketApi.JsApiType.jsapi);
        String jsapi_ticket = jsApiTicket.getTicket();
        String nonce_str = UUID.randomUUID().toString();
        // 注意 URL 一定要动态获取，不能 hardcode.
/*        String url = "http://" + getRequest().getServerName() // 服务器地址
                // + ":"
                // + getRequest().getServerPort() //端口号
                + getRequest().getContextPath() // 项目名称
                + getRequest().getServletPath();// 请求页面或其他地址*/
        String url = getPara("url");
        if (StrKit.notBlank(url)){
            url = url.replace("&amp;", "&");
            String timestamp = Long.toString(System.currentTimeMillis() / 1000);
            // 这里参数的顺序要按照 key 值 ASCII 码升序排序
            //注意这里参数名必须全部小写，且必须有序
            String  str = "jsapi_ticket=" + jsapi_ticket +
                    "&noncestr=" + nonce_str +
                    "&timestamp=" + timestamp +
                    "&url=" + url;

            String signature = HashKit.sha1(str);

            Map<String, String> map = new HashMap<>();
            // 华人老板
           // map.put("appId", ApiConfigKit.getApiConfig().getAppId());
            // 途听
            map.put("appId", PropKit.get("tut.appid"));
            map.put("nonceStr", nonce_str);
            map.put("timestamp", timestamp);
            map.put("url", url);
            map.put("signature", signature);
            map.put("jsapi_ticket", jsapi_ticket);

            System.out.println(map.toString());

            renderJson(R.ok().put(map));
        }else{
            renderJson(R.error("URL参数为空"));
        }
    }

    /**
     * 获取文章素材
     */
    public void getNew(){
        int page = getParaToInt("page",0);
        ApiResult apiResult = WxUtils.batchGetMaterial("news", page*20, 20);

        List<WxMediaArticles> mediaArticlesList = new ArrayList<>();  //文章列表

        List<Map<String ,Object>> wxMedias = apiResult.get("item");

        for (Map<String ,Object> media :wxMedias){
            Map<String, Object> content = (Map<String, Object>) media.get("content");
            List<Map<String, Object>> newItem = (List<Map<String, Object>>) content.get("news_item");

            for (Map<String, Object> map : newItem){
                WxMediaArticles ma = new WxMediaArticles();
                ma.setTitle(String.valueOf(map.get("title")));
                ma.setDescription(String.valueOf(map.get("digest")));
                ma.setUrl(String.valueOf(map.get("url")));
                ma.setPicUrl(String.valueOf(map.get("thumb_url")));


                mediaArticlesList.add(ma);
            }
        }

        renderJson(mediaArticlesList);
    }

    /**
     * 获取图片素材
     */
    public void getImage(){
        int page = getParaToInt("page",0);
        ApiResult apiResult = WxUtils.batchGetMaterial("image", page*20, 20);
        List<Map<String ,Object>> wxMedias = apiResult.get("item");

        renderJson(wxMedias);
    }


    /**
     * 获取公众号菜单
     */
    public void getMenu() {
        ApiResult apiResult = MenuApi.getMenu();
        if (apiResult.isSucceed())
            renderText(apiResult.getJson());
        else
            renderText(apiResult.getErrorMsg());
    }

    /**
     * 删除公众号菜单
     */
    public void deleteMenu() {
        ApiResult apiResult = MenuApi.deleteMenu();
        if (apiResult.isSucceed())
            renderText(apiResult.getJson());
        else
            renderText(apiResult.getErrorMsg());
    }


    /**
     * 创建菜单
     */
    public void createMenu() {
        String str = "{\"button\":[{\"name\":\"1分钟招到好工人\",\"type\":\"view\",\"url\":\"http://hboss.htmlk.cn\"}]}";
        ApiResult apiResult = MenuApi.createMenu(str);
        if (apiResult.isSucceed())
            renderText(apiResult.getJson());
        else
            renderText(apiResult.getErrorMsg());
    }

    /**
     * 发送模板消息
     */
    public void sendMsg() {

        ApiResult apiResult = TemplateMsgApi.send(TemplateData.New()
                // 消息接收者
                .setTouser("ookNixFNwthBIscSkKGUOirEmYXQ")
                // 模板id
                .setTemplate_id("s7JZCn1Sop2dFum_dyDRr5ppStW6AxqBXJ0dOd0shHQ")
                // 模板参数
                .add("first", "模板消息-会员注册！\n", "#999")
                .add("keyword1", "老哥", "#999")
                .add("keyword2", "13888888888", "#999")
                .add("keyword3", "99999021123", "#999")
                .add("remark", "\n模板消息测试gogo。", "#999")
                .build());
        renderText(apiResult.getJson());
    }

}
