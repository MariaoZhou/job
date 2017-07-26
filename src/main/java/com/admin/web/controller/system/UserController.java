package com.admin.web.controller.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.service.system.UserService;
import com.admin.web.validator.system.UserValidator;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.Consts;
import com.rlax.framework.common.Status;
import com.rlax.framework.exception.BusinessException;
import com.rlax.web.base.MessageBean;
import com.rlax.web.model.Role4s;
import com.rlax.web.model.User4s;
import com.rlax.web.model.UserRole4s;

/**
 * 用户管理
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/user")
public class UserController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		User4s info = new User4s();
		info.setName(getPara("name"));
		info.setEmail(getPara("email"));
		
		Page<User4s> list = User4s.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}
	
	public void add() {
		createToken();	
		Map<String, String> listmap = Role4s.dao.findRoleMapOnUse();
		setAttr("listmap", listmap).render("add.html");
	}

	@Before(UserValidator.class)
	public void do_add() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		User4s user = getModel(User4s.class, "info");
		user.setStatus(Status.USER_REGISTER);
		user.setCreatedate(new Date());
		user.setLastUpdAcct(getLoginUser4s().getName());
		user.setLastUpdTime(getDateTime());
		user.setNote("添加系统用户");
		
		Long[] roleIds = getParaValuesToLong("roleIds");
		
		MessageBean message = new MessageBean();
		message.success();
		
		UserService userService = Duang.duang(UserService.class);
		try {
			userService.saveUser(user, roleIds);
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/system/user");
	}
	
	public void update() {
		createToken();	
		Long id = getParaToLong("id");
		User4s info = User4s.dao.findById(id);
		info.remove("pwd", "salt2");
		List<UserRole4s> userRoles = UserRole4s.dao.findByUserID(id);
		
		List<String> ids = new ArrayList<String>();
		for (UserRole4s userRole : userRoles) {
			ids.add(userRole.getRoleId().toString());
		}
		
		Map<String, String> listmap = Role4s.dao.findRoleMapOnUse();
		setAttr("listmap", listmap).setAttr("info", info).setAttr("roleIds", ids).render("update.html");
	}	

	@Before(UserValidator.class)
	public void do_update() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		User4s user = getModel(User4s.class, "info");
		user.setLastUpdAcct(getLoginUser4s().getName());
		user.setLastUpdTime(getDateTime());
		user.setNote("修改系统用户");
		
		Long[] roleIds = getParaValuesToLong("roleIds");
		
		MessageBean message = new MessageBean();
		message.success();
		
		UserService userService = Duang.duang(UserService.class);
		try {
			userService.updateUser(user, roleIds);
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/system/user");
	}
	
	public void delete() {
		User4s.dao.deleteById(getParaToLong("id"));
		MessageBean message = new MessageBean();
		message.success();
		setSessionMessage(message);
		redirect("/system/user");
	}
	
	/**
	 * 个人信息修改
	 */
	public void profile() {
		createToken();	
		User4s user = getLoginUser4s();
		user = User4s.dao.findById(user.getId());
		user.remove("pwd", "salt2");
		setAttr("info", user).render("profile.html");
	}
	
	/**
	 * 确认个人信息修改
	 */
	@Before(UserValidator.class)
	public void do_profile() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		User4s user = getModel(User4s.class, "info");
		user.setLastUpdAcct(getLoginUser4s().getName());
		user.setLastUpdTime(getDateTime());
		user.setNote("修改个人信息");
		
		MessageBean message = new MessageBean();
		message.success();
		
		UserService userService = Duang.duang(UserService.class);
		try {
			userService.updateProfile(user);
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/system/user/profile");
	}

	/**
	 * 个人密码修改
	 */
	public void changepwd() {
		createToken();	
		User4s user = getLoginUser4s();
		user = User4s.dao.findById(user.getId());
		user.remove("pwd", "salt2");
		setAttr("info", user).render("changepwd.html");
	}
	
	/**
	 * 确认个人密码修改
	 */
	@Before(UserValidator.class)
	public void do_changepwd() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		User4s user = getModel(User4s.class, "info");
		user.setLastUpdAcct(getLoginUser4s().getName());
		user.setLastUpdTime(getDateTime());
		user.setNote("修改密码");
		
		String newPwd = getPara("newPwd");
		
		MessageBean message = new MessageBean();
		message.success();
		
		UserService userService = Duang.duang(UserService.class);
		try {
			userService.updatePwd(user, newPwd);
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/system/user/changepwd");
	}
	
	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/system/user");	}

}
