package com.admin.web.controller.weixin;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.weixin.WxUserInfo;
import com.admin.web.util.WxUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Javen
 * 2015年12月5日下午2:20:44
 *
 */
@ControllerBind(controllerKey = "/wx/oauth2")
public class WxRedirectUri extends BaseBussinessController {

	public void index() {
		//用户同意授权，获取code
		String code   = getPara("code");
		if (StrKit.isBlank(code)) {
			renderText("code is Blank!");
		}
		String appId  = ApiConfigKit.getAppId();
		String secret = ApiConfigKit.getApiConfig().getAppSecret();
		SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(appId, secret, code);

		String openId = snsAccessToken.getOpenid();
		String token  = snsAccessToken.getAccessToken();

		//拉取用户信息(需scope为 snsapi_userinfo)
		ApiResult apiResult = SnsApi.getUserInfo(token, openId);
		System.out.println("apiResult: " + apiResult);

        try {
            WxUserInfo userInfo = WxUtils.wxUserInfo(apiResult);
            renderText("WxUserInfo:" + userInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


	}
	
	
	public void oauth() {
		String appId = ApiConfigKit.getAppId();
        String redirectUri = null;
        try {
            redirectUri = URLEncoder.encode("http://test.13701918.com/wx/oauth2", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String state = System.currentTimeMillis() + "";

		String url = SnsAccessTokenApi.getAuthorizeURL(appId, redirectUri, state, false);
        System.out.println("url = " + url);
        renderText(url);
		//redirect(url);
	}

	@Override
	public void onExceptionError(Exception e) {

	}
}
