package com.admin.web.base;

import com.rlax.framework.plugin.shiro.ShiroUtils;
import com.rlax.web.base.BaseController;
import com.rlax.web.model.User4s;

/**
 * 控制器业务基类
 * @author Rlax
 *
 */
public abstract class BaseBussinessController extends BaseController {
	
	/**
	 * 获取系统登录用户
	 * @return
	 */
	public User4s getLoginUser4s() {
		return ShiroUtils.getLoginUser4s();
	}
}
