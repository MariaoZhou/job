package com.admin.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import com.admin.web.base.BaseBussinessController;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.rlax.framework.plugin.shiro.MuitiLoginToken;
import com.rlax.framework.plugin.shiro.ShiroUtils;
import com.rlax.web.model.User4s;
import com.rlax.web.validator.LoginValidator;

/**
 * 核心控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/")
public class MainController extends BaseBussinessController {

	public void index() {
		render("index.html");
	}
	
	public void login() {
		if (SecurityUtils.getSubject().isAuthenticated()) {
			redirect("/");
		} else {
			render("login.html");
		}
	}
	
	public void captcha() {
		renderCaptcha();
	}
	
	@Before(LoginValidator.class)
	public void do_login() {
		String loginName = getPara("loginName");
		String pwd = getPara("password");
		
		MuitiLoginToken token = new MuitiLoginToken(loginName, pwd);
		Subject subject = SecurityUtils.getSubject();
		
		boolean flag = false;
		try {
			if (!subject.isAuthenticated()) {
				token.setRememberMe(false);
				subject.login(token);
				User4s u = User4s.dao.findByName(loginName);
				
				ShiroUtils.refreshSessionByUserId(u.getId());
			}
			if (getParaToBoolean("rememberMe") != null && getParaToBoolean("rememberMe")) {
				setCookie("loginName", loginName, 60 * 60 * 24 * 7);
			} else {
				removeCookie("loginName");
			}
		} catch (UnknownAccountException une) {
			keepPara("loginName", "password");
			setAttr("loginNameMsg", "用户名不存在");
			flag = true;
		} catch (IncorrectCredentialsException ine) {
			keepPara("loginName", "password");
			setAttr("loginNameMsg", "用户名密码错误");		
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			keepPara("loginName", "password");
			setAttr("loginNameMsg", "服务器异常，请稍后重试");	
			flag = true;
		}
		
		if (flag) {
			render("login.html");
		} else {
			String backUrl = "/";
			SavedRequest saveRequest = WebUtils.getAndClearSavedRequest(getRequest());
			if (saveRequest != null && saveRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
				backUrl = saveRequest.getRequestUrl();
			}
			redirect(backUrl);
		}
	}
	
	public void logout() {
		if (SecurityUtils.getSubject().isAuthenticated()) {
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
			redirect("/");
		} else {
			redirect("/");
		}
	}

	@Override
	public void onExceptionError(Exception e) {
		render("login.html");
	}
	
}
