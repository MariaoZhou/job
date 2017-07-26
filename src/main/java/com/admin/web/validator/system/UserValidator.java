package com.admin.web.validator.system;

import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.rlax.web.model.Role4s;
import com.rlax.web.model.User4s;

/**
 * 用户校验
 * @author Rlax
 *
 */
public class UserValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_add")) {
			validateRequiredString("info.name", "nameMsg", "请输入用户名");
			validateRequiredString("info.pwd", "pwdMsg", "请输入密码");
			validateRequiredString("roleIds", "roleIdsMsg", "请输入角色");
			validateRequiredString("info.status", "statusMsg", "请选择状态");
		} else if (methodName.equals("do_update")) {
			validateRequiredString("info.name", "nameMsg", "请输入用户名");
			validateRequiredString("info.pwd", "pwdMsg", "请输入密码");
			validateRequiredString("roleIds", "roleIdsMsg", "请输入角色");
			validateRequiredString("info.status", "statusMsg", "请选择状态");	
		} else if (methodName.equals("do_profile")) {
			validateRequiredString("info.name", "nameMsg", "请输入用户名");
			validateRequiredString("info.pwd", "pwdMsg", "请输入密码");
		} else if (methodName.equals("do_changepwd")) {
			validateRequiredString("info.name", "nameMsg", "请输入用户名");
			validateRequiredString("info.pwd", "pwdMsg", "请输入当前密码");
			validateRequiredString("newPwd", "newPwdMsg", "请输入新密码");
		}
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(User4s.class, "info");
		c.keepPara();
		
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_add")) {
			c.createToken();
			Map<String, String> listmap = Role4s.dao.findRoleMapOnUse();
			c.setAttr("listmap", listmap).render("add.html");
		} else if (methodName.equals("do_update")) {
			c.createToken();
			Map<String, String> listmap = Role4s.dao.findRoleMapOnUse();
			c.setAttr("listmap", listmap).render("update.html");
		} else if (methodName.equals("do_profile")) {
			c.createToken();
			c.render("profile.html");
		} else if (methodName.equals("do_changepwd")) {
			c.createToken();
			c.render("changepwd.html");
		}			
	}

}
