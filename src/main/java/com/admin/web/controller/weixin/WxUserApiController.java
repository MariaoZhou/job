package com.admin.web.controller.weixin;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobMember;
import com.admin.web.model.weixin.WxUserInfo;
import com.admin.web.service.job.JobMemberService;
import com.admin.web.util.R;
import com.admin.web.util.WxUtils;
import com.jfinal.aop.Duang;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
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
        //用户同意授权，获取code
        String code = getPara("code");
        if (StrKit.isBlank(code)) {
            renderJson(R.error("未获取到 weixin code 信息"));
            return;
        }
        String appId  = PropKit.get("service.appid");
        String secret = PropKit.get("service.appSecret");
        SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(appId, secret, code);

        String openId = snsAccessToken.getOpenid();
        String token  = snsAccessToken.getAccessToken();

        //拉取用户信息(需scope为 snsapi_userinfo)
        ApiResult apiResult = SnsApi.getUserInfo(token, openId);

        try {
            WxUserInfo wxUserInfo = WxUtils.wxUserInfo(apiResult);

            JobMember member = new JobMember();
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
            jobService.saveAndUpdateMember(member);

            if (StrKit.notBlank(state)){
                state = state.replace("okid",openId);
                redirect(state);
            }else {
                // TODO 重定向 默认错误页
            }

        } catch (Exception e) {
            e.printStackTrace();
            renderJson(R.error("用户信息错误, 请确定参数是否正确"));
        }



    }


	/**
	 * 通过 openid 查询 微信用户信息(不再使用)
	 */
	public void findWxUserInfo () {
		String openid = getPara("openid");
		if (StrKit.notBlank(openid)){
			ApiResult apiResult = UserApi.getUserInfo(openid);

			try {
				WxUserInfo wxUserInfo = WxUtils.wxUserInfo(apiResult);
				System.out.println("wxUserInfo ========== " + wxUserInfo.toString());
				JobMember member = new JobMember();
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
				member = jobService.saveAndUpdateMember(member);
				renderJson(R.ok().put(member));

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				renderJson(R.error("用户信息错误, 请确定参数是否正确"));
			}
		}else {
			renderJson(R.error("未接收到用户标识"));
		}

	}


	@Override
	public void onExceptionError(Exception e) {renderJson(R.error("接口调用异常"));}

}
