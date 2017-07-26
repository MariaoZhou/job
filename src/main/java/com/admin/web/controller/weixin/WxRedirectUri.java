package com.admin.web.controller.weixin;

import com.admin.web.base.BaseBussinessController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.*;

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

		System.out.println("snsAccessToken: " + snsAccessToken);
		String openId = snsAccessToken.getOpenid();
		String token  = snsAccessToken.getAccessToken();

		//拉取用户信息(需scope为 snsapi_userinfo)
		ApiResult apiResult = SnsApi.getUserInfo(token, openId);
		System.out.println("openId: " + openId);

		String nickname = apiResult.get("nickname");
		String sex = apiResult.get("sex");
		String city = apiResult.get("city");
		String province = apiResult.get("province");
		String country = apiResult.get("country");
		String headimgurl = apiResult.get("headimgurl");

		System.out.println("nickname:"+nickname);
	/*	try {
			System.out.println("nickname:"+URLEncoder.encode(nickname, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		System.out.println("sex:" + sex);//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
		System.out.println("city:" + city);//城市
		System.out.println("province:" + province);//省份
		System.out.println("country:" + country);//国家
		System.out.println("headimgurl:" + headimgurl);

		renderText("apiResult:" + apiResult);
	}
	
	
	public void oauth() {
		String appId = ApiConfigKit.getAppId();
		String redirectUri = null;

			//redirectUri = URLEncoder.encode(PropKit.get("weixin.url")+"/oauth2", "UTF-8");

		String state = System.currentTimeMillis() + "";

		String url = SnsAccessTokenApi.getAuthorizeURL(appId, redirectUri, state, false);
		redirect(url);
	}

	@Override
	public void onExceptionError(Exception e) {

	}
}
