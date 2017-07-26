package com.admin.web.controller.system;

import java.util.Date;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.validator.system.PageValidator;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.CacheKey;
import com.rlax.framework.common.Consts;
import com.rlax.framework.common.Status;
import com.rlax.framework.exception.BusinessException;
import com.rlax.framework.support.cache.CacheExtKit;

/**
 * 页面控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/page")
public class PageController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		
		com.rlax.web.model.Page info = new com.rlax.web.model.Page();
		info.setName(getPara("name"));
		info.setNo(getPara("no"));
		
		Page<com.rlax.web.model.Page> list = com.rlax.web.model.Page.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}
	
	public void add() {
		createToken();
		render("add.html");
	}

	@Before(PageValidator.class)
	public void do_add() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		com.rlax.web.model.Page info = getModel(com.rlax.web.model.Page.class, "info");
		info.setNo(getLoginUser4s().getName());
		info.setCreateDate(new Date());
		info.setPreContent("");
		info.setStatus(Status.COMMON_USE);
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		info.setNote("添加页面");
		info.save();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/page");
	}
	
	/**
	 * 刷新缓存
	 */
	public void refreshCache() {
		CacheExtKit.removeAll(CacheKey.CACHE_PAGE);
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/page");
	}
	
	/**
	 * 预览
	 */
	public void preview() {
		String id = getPara("id");
		com.rlax.web.model.Page info = com.rlax.web.model.Page.dao.findById(id);
		setAttr("info", info).renderHtml(info.getContent());
	}

	/**
	 * 删除
	 */
	public void delete() {
		String id = getPara("id");
		com.rlax.web.model.Page.dao.deleteById(id);
		setSessionSuccessMessage("操作成功");
		redirect("/system/page");
	}
	
	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/system/page");
	}

}
