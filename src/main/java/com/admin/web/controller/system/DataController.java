package com.admin.web.controller.system;

import java.util.Date;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.validator.system.DataValidator;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.CacheKey;
import com.rlax.framework.common.Consts;
import com.rlax.framework.exception.BusinessException;
import com.rlax.framework.support.cache.CacheExtKit;
import com.rlax.web.model.Data;

/**
 * 数据字典控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/data")
public class DataController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		Data info = new Data();
		info.setType(getPara("type"));
		info.setTypeDesc(getPara("typeDesc"));
		
		Page<Data> list = Data.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}
	
	public void add() {
		createToken();
		render("add.html");
	}

	@Before(DataValidator.class)
	public void do_add() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		Data info = getModel(Data.class, "info");
		info.setCreateDate(new Date());
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		info.setNote("添加数据字典");
		info.save();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/data");
	}
	
	public void update() {
		createToken();		
		Long id = getParaToLong("id");
		Data info = Data.dao.findById(id);
		setAttr("info", info).render("update.html");
	}
	
	public void do_update() {
		if (!validateToken()) {
			throw new BusinessException("您已成功提交表单，请勿重复提交");
		}
		
		Data info = getModel(Data.class, "info");
		
		info.setLastUpdAcct(getLoginUser4s().getName());
		info.setLastUpdTime(getDateTime());
		info.setNote("修改数据字典");
		info.update();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/data");
	}
	
	public void delete() {
		String id = getPara("id");
		Data info = Data.dao.findById(id);
		info.delete();
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/data");
	}
	
	/**
	 * 刷新缓存
	 */
	public void refreshCache() {
		CacheExtKit.removeAll(CacheKey.CACHE_KEYVALUE);
		
		setSessionSuccessMessage("操作成功");
		redirect("/system/data");
	}
	
	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/system/data");
	}

}
