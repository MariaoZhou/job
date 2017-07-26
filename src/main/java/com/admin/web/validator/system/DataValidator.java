package com.admin.web.validator.system;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.rlax.web.model.Data;

/**
 * 数据字典校验
 * @author Rlax
 *
 */
public class DataValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequiredString("info.code", "codeMsg", "请输入代码");
		validateRequiredString("info.codeDesc", "codeDescMsg", "请输入代码描述");
		validateRequiredString("info.type", "typeMsg", "请输入类型编码");
		validateRequiredString("info.typeDesc", "typeDescMsg", "请输入类型描述");	
		validateRequiredString("info.status", "statusMsg", "请选择状态");
		validateRequiredString("info.orderNo", "orderNoMsg", "请输入排序号");			
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(Data.class, "info");
		
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_add")) {
			c.createToken();	
			c.render("add.html");
		} else if (methodName.equals("do_update")) {
			c.createToken();	
			c.render("update.html");
		}
	}

}
