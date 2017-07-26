package com.admin.web.util;

import com.admin.web.model.weixin.WxUserInfo;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.weixin.sdk.utils.JsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信 工具类
 */
public class WxUtils {

    /**
     * UTF-8编码
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户信息 Json 转 实体类
     * @param r
     * @return
     */
    public static WxUserInfo wxUserInfo(ApiResult r) throws Exception{

        System.out.println("ApiResult :::: "+r.toString());

        String city = "";
        if (r.get("city")!=null){
            city = r.get("city").toString();
        }

        String country = "";
        if (r.get("country")!=null){
            country = r.get("country").toString();
        }

        String groupid = "";
        if (r.get("groupid")!=null){
            groupid = r.get("groupid").toString();
        }

        String headimgurl = "";
        if (r.get("headimgurl") != null){
            headimgurl = r.get("headimgurl").toString();
        }

        String language = "";
        if ( r.get("language") != null){
            language = r.get("language").toString();
        }

        String nickname = "";
        if (r.get("nickname") != null){
            nickname = r.get("nickname").toString();
        }

        String openid = "";
        if (r.get("openid") != null){
            openid = r.get("openid").toString();
        }

        String province = "";
        if (r.get("province") != null){
            province = r.get("province").toString();
        }

        String remark = "";
        if ( r.get("remark") != null){
            remark =  r.get("remark").toString();
        }

        String sex = null;
        if (r.get("sex") != null){
            sex = r.get("sex").toString();
        }

        //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

        if (StrKit.notBlank(sex)) {
            int sexInt=Integer.parseInt(sex);
            if (sexInt==1) {
                sex="男";
            }else if (sexInt==2) {
                sex="女";
            }else {
                sex="未知";
            }
        }
        else {
            sex="未知";
        }


        String subscribe = null;
        if (r.get("subscribe") !=null){
            subscribe = r.get("subscribe").toString();
        }

        String subscribe_time = null;
        if (r.get("subscribe_time") !=null){
            subscribe_time = r.get("subscribe_time").toString();
        }
        ;

        if (subscribe_time!=null && !subscribe_time.equals("")) {
            subscribe_time=DateUtils.formatDateTime(new Date(Long.parseLong(subscribe_time) * 1000L));
        }

        WxUserInfo userInfo = new WxUserInfo(city, country, groupid, headimgurl, language, nickname, openid, province, remark, sex, subscribe, subscribe_time);
        return userInfo;
    }

    // 获取素材列表
    private static String batchget_material_url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";

    /**
     * 获取素材列表
     * @param mediaType 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     * @return ApiResult 返回信息
     */
    public static ApiResult batchGetMaterial(String mediaType, int offset, int count) {
        String url = batchget_material_url + AccessTokenApi.getAccessTokenStr();

        if(offset < 0) offset = 0;
        if(count > 20) count = 20;
        if(count < 1) count = 1;

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("type", mediaType);
        dataMap.put("offset", offset);
        dataMap.put("count", count);

        String jsonResult = HttpUtils.post(url, JsonUtils.toJson(dataMap));
        return new ApiResult(jsonResult);
    }
}
