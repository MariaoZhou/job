package com.admin.web.validator.system;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.rlax.framework.common.ZTree;
import com.rlax.web.model.Res4s;
import com.rlax.web.model.Role4s;

/**
 * 角色校验
 * @author Rlax
 *
 */
public class RoleValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequiredString("info.name", "nameMsg", "请输入角色名称");
		validateRequiredString("info.des", "desMsg", "请输入角色描述");
		validateRequiredString("info.seq", "seqMsg", "请输入排序号");		
		validateRequiredString("info.status", "statusMsg", "请选择状态");
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(Role4s.class, "info");
		c.keepPara();
		
		String methodName = getActionMethod().getName();
		if (methodName.equals("do_add")) {
			List<ZTree> zTree = Res4s.dao.findzTreeOnUse();
			c.setAttr("urls", JSON.toJSONString(zTree)).render("add.html");
		} else if (methodName.equals("do_update")) {
			Long roleId = c.getParaToLong("info.id");
			List<ZTree> zTree = Res4s.dao.findzTreeByRoleIdOnUse(roleId);
			c.setAttr("urls", JSON.toJSONString(zTree)).render("update.html");
		}
	}

}
