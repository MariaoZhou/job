package com.admin.web.controller.system;

import java.util.Date;
import java.util.List;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.service.system.RoleService;
import com.admin.web.validator.system.RoleValidator;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.Consts;
import com.rlax.framework.common.ZTree;
import com.rlax.framework.exception.BusinessException;
import com.rlax.web.base.MessageBean;
import com.rlax.web.model.Res4s;
import com.rlax.web.model.Role4s;

/**
 * 角色控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/role")
public class RoleController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		
		Role4s info = new Role4s();
		info.setName(getPara("name"));
		info.setDes(getPara("des"));
		
		Page<Role4s> list = Role4s.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		
		keepPara();
		setAttr("page", list);
		render("main.html");
	}
	
	public void add() {
		createToken();	
		List<ZTree> zTree = Res4s.dao.findzTreeOnUse();
		setAttr("urls", JSON.toJSONString(zTree)).render("add.html");
	}
	
	@Before(RoleValidator.class)
	public void do_add() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		Role4s info = getModel(Role4s.class, "info");
		String resIds = getPara("resIds");
		
		info.setCreatedate(new Date());
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		info.setNote("添加角色");
		
		MessageBean message = new MessageBean();
		message.success();
		
		RoleService roleService = Duang.duang(RoleService.class);
		try {
			roleService.saveRole(info, resIds);
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/system/role");
	}
	
	public void update() {
		createToken();	
		Long roleId = getParaToLong("id");
		List<ZTree> zTree = Res4s.dao.findzTreeByRoleIdOnUse(roleId);
		Role4s info = Role4s.dao.findById(roleId);
		setAttr("info", info).setAttr("urls", JSON.toJSONString(zTree)).render("update.html");
	}
	
	@Before(RoleValidator.class)
	public void do_update() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		Role4s info = getModel(Role4s.class, "info");
		String resIds = getPara("resIds");
		
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		
		MessageBean message = new MessageBean();
		message.success();
		
		RoleService roleService = Duang.duang(RoleService.class);
		try {
			roleService.updateRole(info, resIds);
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/system/role");
	}
	
	public void delete() {
		String id = getPara("id");
		Role4s info = Role4s.dao.findById(id);
		info.delete();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/role");
	}
	
	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/system/role");		
	}

}
