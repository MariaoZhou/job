package com.admin.web.controller.weixin;

import app.App;
import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.UserInfo;
import com.admin.web.model.weixin.WxUserInfo;
import com.admin.web.util.R;
import com.admin.web.util.WxUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.*;

import java.util.Date;

/**
 *  微信公众 用户 接口Controller
 * <p>Description: JobMessageController </p>
 * @author:  xutie
 * @created: 2017/6/15
 * @version: 1.0
 */
@ControllerBind(controllerKey = "/wx/user")
public class WxUserApiController extends BaseBussinessController {

    /**
     * 用户授权登录
     */
    public void login(){

        String state = getPara("state","");
        String appId = getPara("appid","");
        System.out.println(" login appId = " + appId);
        //用户同意授权，获取code
        String code = getPara("code");
        if (StrKit.isBlank(code)) {
            renderJson(R.error("未获取到 weixin code 信息"));
            return;
        }
        ApiConfig apiConfig = ApiConfigKit.getApiConfig(appId);
        System.out.println("getAppId = " + apiConfig.getAppId());
        System.out.println("getAppSecret = " + apiConfig.getAppSecret());
        SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(apiConfig.getAppId(), apiConfig.getAppSecret(), code);

        String openId = snsAccessToken.getOpenid();
        String token  = snsAccessToken.getAccessToken();

        //拉取用户信息(需scope为 snsapi_userinfo)
        ApiResult apiResult = SnsApi.getUserInfo(token, openId);
        System.out.println("apiResult = " + apiResult);
        try {
            System.out.println("登录的用户 openid" + openId);
            System.out.println("登录的用户 state" + state);
            System.out.println("登录的用户 apiResult" + apiResult);
            WxUserInfo wxUserInfo = WxUtils.wxUserInfo(apiResult);

            UserInfo user = UserInfo.dao.findFirst("select * from user_info where openid = ?", openId);

            if (user == null ){
                user = new UserInfo();
                user.setHead(wxUserInfo.getHeadimgurl());
                user.setName(wxUserInfo.getNickname());
                user.setOpenId(wxUserInfo.getOpenid());

                user.setCreateDate(new Date());
                user.setUpdateDate(new Date());
                user.save();
            }

            /*JobMember member = new JobMember();
            member.setOpenId(wxUserInfo.getOpenid());
            //member.setName(userJson.getString("nickName"));
            member.setLanguage(wxUserInfo.getLanguage());
            member.setImage(wxUserInfo.getHeadimgurl());	//头像
            member.setCountry(wxUserInfo.getCountry());	//国家
            member.setSex(wxUserInfo.getSex());			//性别 值为1时是男性，值为2时是女性，值为0时是未知
            member.setRole("0");						//默认角色  应聘者
            member.setState("0");						//状态 激活
            member.setCreateDate(new Date());
            member.setMobileCode(wxUserInfo.getProvince());	//城市 拼音 全拼 TODO 暂时使用 mobileCode字段

            JobMemberService jobService = Duang.duang(JobMemberService.class);
            System.out.println("jobmember =======" + member.toString());
            jobService.saveAndUpdateMember(member);*/

            // 重定向 前端url 替换okid 关键字为 userId
            if (StrKit.notBlank(state)){
                state = state.replace("okid",user.getId().toString());
                redirect(state);
            }else {
                // TODO 重定向 默认错误页
            }

        } catch (Exception e) {
            e.printStackTrace();
            renderJson(R.error("用户信息错误, 请确定参数是否正确"));
        }



    }


	@Override
	public void onExceptionError(Exception e) {renderJson(R.error("接口调用异常"));}

}
