package com.admin.web.controller.system;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.validator.system.ResValidator;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.Consts;
import com.rlax.framework.common.Status;
import com.rlax.framework.exception.BusinessException;
import com.rlax.framework.plugin.shiro.ShiroCacheUtils;
import com.rlax.web.model.Res4s;

/**
 * 资源控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/res")
public class ResController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		
		Res4s info = new Res4s();
		info.setPid(getParaToLong("pid"));
		info.setName(getPara("name"));
		info.setUrl(getPara("url"));
		
		Page<Res4s> list = Res4s.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		
		Res4s pinfo = Res4s.dao.findById(info.getPid());
		keepPara();
		if (pinfo == null) {
			setAttr("pid", 0L);
			setAttr("pname", "ROOT");
			setAttr("bid", 0L);
		} else {
			setAttr("pid", pinfo.getId());
			setAttr("pname", pinfo.getName());
			setAttr("bid", pinfo.getPid());
		}

		setAttr("page", list);
		render("main.html");
	}
	
	public void add() {
		createToken();	
		String pid = getPara("pid");
		Res4s info = new Res4s();
		Res4s pinfo = Res4s.dao.findById(pid);
		
		String pname = "ROOT";
		if (pinfo == null) {
			info.setPid(0L);
		} else {
			info.setPid(pinfo.getId());
			pname = pinfo.getName();
		}
		
		setAttr("info", info).setAttr("pname", pname).render("add.html");
	}
	
	@Before(ResValidator.class)
	public void do_add() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		Res4s info = getModel(Res4s.class, "info");
		info.setStatus(Status.COMMON_USE);
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		info.setNote("添加资源");
		info.save();
		
		// 更新缓存里Urls
		ShiroCacheUtils.updateResUrls();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/res/index?pid=" + info.getPid());
	}
	
	public void update() {
		createToken();	
		String id = getPara("id");
		Res4s info = Res4s.dao.findById(id);
		Res4s pinfo = Res4s.dao.findById(info.getPid());
				
		String pname = "ROOT";
		if (pinfo == null) {
			info.setPid(0L);
		} else {
			info.setPid(pinfo.getId());
			pname = pinfo.getName();
		}
		
		setAttr("info", info).setAttr("pname", pname).render("update.html");
	}
	
	@Before(ResValidator.class)
	public void do_update() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		Res4s info = getModel(Res4s.class, "info");
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		info.setNote("修改资源");
		info.update();
		
		// 更新缓存里Urls
		ShiroCacheUtils.updateResUrls();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/res/index?pid=" + info.getPid());
	}
	
	public void delete() {
		String id = getPara("id");
		Res4s info = Res4s.dao.findById(id);
		info.delete();
		
		// 更新缓存里Urls
		ShiroCacheUtils.updateResUrls();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/res/index?pid=" + info.getPid());
	}
	
	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/system/res");
	}

}
