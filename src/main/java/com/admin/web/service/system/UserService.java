package com.admin.web.service.system;


import java.util.ArrayList;
import java.util.List;

import com.admin.web.base.BaseBussinessService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.rlax.framework.exception.BusinessException;
import com.rlax.framework.plugin.shiro.ShiroUtils;
import com.rlax.web.model.User4s;
import com.rlax.web.model.UserRole4s;

/**
 * 用户服务
 * @author Rlax
 *
 */
public class UserService extends BaseBussinessService {

	/**
	 * 保存用户
	 * @param user
	 * @param roleIds 
	 */
	@Before(Tx.class)
	public void saveUser(User4s user, Long[] roleIds) {
		if (User4s.dao.findByName(user.getName()) != null) {
			throw new BusinessException("用户名" + user.getName() + "已经存在");
		}
	
		user.encrypt().save();
		
		List<UserRole4s> list = new ArrayList<UserRole4s>();
		for (Long roleId : roleIds) {
			UserRole4s userRole = new UserRole4s();
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			list.add(userRole);
		}
		Db.batchSave(list, list.size());
	}

	/**
	 * 更新用户
	 * @param user
	 * @param roleIds 
	 */
	@Before(Tx.class)
	public void updateUser(User4s user, Long[] roleIds) {
		User4s info = User4s.dao.findById(user.getId());
		if (info == null) {
			throw new BusinessException("用户名" + user.getName() + "不存在");
		}
	
		info.setName(user.getName());
		info.setPwd(user.getPwd());
		info.setEmail(user.getEmail());
		info.setPhone(user.getPhone());
		info.setStatus(user.getStatus());
		info.setLastUpdAcct(user.getLastUpdAcct());
		info.setLastUpdTime(user.getLastUpdTime());
		info.setNote(user.getNote());
		info.encrypt().update();
		
		UserRole4s.dao.deleteByUserId(info.getId());
		List<UserRole4s> list = new ArrayList<UserRole4s>();
		for (Long roleId : roleIds) {
			UserRole4s userRole = new UserRole4s();
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			list.add(userRole);
		}
		Db.batchSave(list, list.size());
	}
	
	/**
	 * 更新个人信息
	 * @param user
	 */
	@Before(Tx.class)
	public void updateProfile(User4s user) {
		User4s info = User4s.dao.findById(user.getId());
		if (info == null) {
			throw new BusinessException("用户名" + user.getName() + "不存在");
		}
	
		if (!ShiroUtils.checkPwd(user, info)) {
			throw new BusinessException("登录密码不正确");
		}
		
		info.setPwd(user.getPwd());
		info.setEmail(user.getEmail());
		info.setPhone(user.getPhone());
		info.setLastUpdAcct(user.getLastUpdAcct());
		info.setLastUpdTime(user.getLastUpdTime());
		info.setNote(user.getNote());
		info.encrypt().update();
	}
	
	/**
	 * 修改密码
	 * @param user
	 * @param newPwd 
	 */
	@Before(Tx.class)
	public void updatePwd(User4s user, String newPwd) {
		User4s info = User4s.dao.findById(user.getId());
		if (info == null) {
			throw new BusinessException("用户名" + user.getName() + "不存在");
		}
	
		if (!ShiroUtils.checkPwd(user, info)) {
			throw new BusinessException("当前登录密码不正确");
		}
		
		info.setPwd(newPwd);
		info.setLastUpdAcct(user.getLastUpdAcct());
		info.setLastUpdTime(user.getLastUpdTime());
		info.setNote(user.getNote());
		info.encrypt().update();
	}
}
