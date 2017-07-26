package com.admin.web.controller.system;

import com.admin.web.base.BaseBussinessController;
import com.jfinal.ext.route.ControllerBind;

/**
 * 监控控制器
 * @author Rlax
 *
 */
@ControllerBind(controllerKey = "/system/monitor")
public class MonitorController extends BaseBussinessController {

	/**
	 * druid监控
	 */
	public void druid() {
		render("druid.html");
	}
	
	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
