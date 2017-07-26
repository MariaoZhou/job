package com.admin.web.validator.system;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * 页面校验
 * @author Rlax
 *
 */
public class PageValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequiredString("info.name", "nameMsg", "请输入页面名称");
		validateRequiredString("info.desc", "descMsg", "请输入页面描述");
	}

	@Override
	protected void handleError(Controller c) {
		c.keepPara();
		
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_add")) {
			c.render("add.html");
		} else if (methodName.equals("do_update")) {
			c.render("update.html");
		}
	}

}
