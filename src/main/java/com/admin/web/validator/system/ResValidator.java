package com.admin.web.validator.system;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.rlax.web.model.Res4s;

/**
 * 资源校验
 * @author Rlax
 *
 */
public class ResValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequiredString("info.name", "nameMsg", "请输入资源名称");
		validateRequiredString("info.url", "urlMsg", "请输入资源URL");
		validateRequiredString("info.des", "desMsg", "请输入资源描述");
		validateRequiredString("info.type", "typeMsg", "请输入资源类型");	
		validateRequiredString("info.seq", "seqMsg", "请输入排序号");		
		
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_update")) {
			validateRequiredString("info.status", "statusMsg", "请选择状态");
		}
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(Res4s.class, "info");
		c.keepPara();
		
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_add")) {
			c.render("add.html");
		} else if (methodName.equals("do_update")) {
			c.render("update.html");
		}
	}

}
