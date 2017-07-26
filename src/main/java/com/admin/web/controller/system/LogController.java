package com.admin.web.controller.system;


import com.admin.web.base.BaseBussinessController;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.Consts;
import com.rlax.web.model.Log4s;

/**
 * 日志控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/log")
public class LogController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		Log4s info = new Log4s();
		
		Page<Log4s> list = Log4s.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}
	
	@Override
	public void onExceptionError(Exception e) {
		
	}

}
