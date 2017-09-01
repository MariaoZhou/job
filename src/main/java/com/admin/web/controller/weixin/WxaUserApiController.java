package com.admin.web.controller.weixin;

import app.App;
import com.admin.web.base.BaseBussinessController;
import com.admin.web.util.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.jfinal.wxaapp.api.WxaUserApi;

/**
 *  微信小程序 用户 接口Controller
 * <p>Description: JobMessageController </p>
 * @author:  xutie
 * @created: 2017/6/15
 * @version: 1.0
 */
@ControllerBind(controllerKey = "/wx/wxa")
public class WxaUserApiController extends BaseBussinessController {

	// 微信用户接口api
	protected WxaUserApi wxaUserApi = Duang.duang(WxaUserApi.class);

	/**
	 * 登陆接口
	 */
	public void login() {
		String jsCode = getPara("code");
		System.out.println("jsCode = " + jsCode);
		if (StrKit.isBlank(jsCode)) {
			renderJson(R.error().put("code is blank"));
			return;
		}
		// 获取SessionKey
		ApiResult apiResult = wxaUserApi.getSessionKey(jsCode);
		System.out.println("apiResult = " + apiResult);
		// 返回{"session_key":"nzoqhc3OnwHzeTxJs+inbQ==","expires_in":2592000,"openid":"oVBkZ0aYgDMDIywRdgPW8-joxXc4"}
		if (!apiResult.isSucceed()) {
			renderJson(apiResult.getJson());
			return;
		}
		// 利用 appId 与 accessToken 建立关联，支持多账户
		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
		String sessionId = StrKit.getRandomUUID();


		accessTokenCache.set("wxa:session:" + sessionId, apiResult.getJson());
		renderJson(R.ok().put("sessionId", sessionId));
	}

	/**
	 * 服务端解密用户信息接口
	 * 获取unionId
	 */
	public void info() {
		String signature = getPara("signature");
		String rawData = getPara("rawData").replace("&quot;", "\"");

		String encryptedData = getPara("encryptedData");
		String iv = getPara("iv");

		// 参数空校验 不做演示
		// 利用 appId 与 accessToken 建立关联，支持多账户
		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
		String sessionId = getHeader("wxa-sessionid");
		if (StrKit.isBlank(sessionId)) {
			renderJson(R.error().put("wxa_session Header is blank"));
			return;
		}
		String sessionJson = accessTokenCache.get("wxa:session:" + sessionId);
		if (StrKit.isBlank(sessionJson)) {
			renderJson(R.error().put("wxa_session sessionJson is blank"));
			return;
		}
		ApiResult sessionResult = ApiResult.create(sessionJson);
		// 获取sessionKey
		String sessionKey = sessionResult.get("session_key");
		if (StrKit.isBlank(sessionKey)) {
			renderJson(R.error().put("sessionKey is blank"));
			return;
		}
		// 用户信息校验
		boolean check = wxaUserApi.checkUserInfo(sessionKey, rawData, signature);
		if (!check) {
			renderJson(R.error().put("UserInfo check fail"));
			return;
		}
		// 服务端解密用户信息
		ApiResult apiResult = wxaUserApi.getUserInfo(sessionKey, encryptedData, iv);
		if (!apiResult.isSucceed()) {

			System.out.println("apiResult.getJson() = " + apiResult.getJson());
			renderJson(apiResult.getJson());
			return;
		}
		// 如果开发者拥有多个移动应用、网站应用、和公众帐号（包括小程序），可通过unionid来区分用户的唯一性
		// 同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。
		String unionId = apiResult.get("unionId");

		JSONObject userJson =  JSON.parseObject(apiResult.getJson());
		/*JobMember member = new JobMember();
		member.setOpenId(userJson.getString("openId"));
		//member.setName(userJson.getString("nickName"));
		member.setLanguage(userJson.getString("language"));
		member.setImage(userJson.getString("avatarUrl"));	//头像
		member.setCountry(userJson.getString("country"));	//国家
		member.setSex(userJson.getString("gender"));		////性别 值为1时是男性，值为2时是女性，值为0时是未知
		member.setRole("0");		//默认角色  应聘者
		member.setState("0");		//状态 激活
		member.setCreateDate(new Date());
		member.setMobileCode(userJson.getString("province"));	//城市 拼音 全拼 TODO 暂时使用 mobileCode字段

		JobMemberService jobService = Duang.duang(JobMemberService.class);
		try {
			System.out.println("jobmember =======" + member.toString());
			member = jobService.saveAndUpdateMember(member);
			renderJson(R.ok().put(member));
		}catch (Exception e){
			System.out.println(e.getMessage());
			renderJson(R.error("保存用户错误"));
		}
*/
	}

	@Override
	public void onExceptionError(Exception e) {
		e.printStackTrace();
		log.error(e.getMessage());
		redirect(App.APP_URL);
	}

}
