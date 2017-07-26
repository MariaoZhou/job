package com.admin.web.service.system;

import java.util.ArrayList;
import java.util.List;

import com.admin.web.base.BaseBussinessService;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.rlax.framework.exception.BusinessException;
import com.rlax.framework.plugin.shiro.ShiroCacheUtils;
import com.rlax.framework.plugin.shiro.ShiroUtils;
import com.rlax.web.model.Role4s;
import com.rlax.web.model.RoleRes4s;

/**
 * 角色服务
 * @author Rlax
 *
 */
public class RoleService extends BaseBussinessService {

	/**
	 * 保存角色
	 * @param info
	 * @param resIds
	 */
	@Before(Tx.class)
	public void saveRole(Role4s info, String resIds) {
		if (Role4s.dao.findById(info.getId()) != null) {
			throw new BusinessException("角色名称" + info.getName() + "已经存在");
		}
		
		info.save();
		
		List<RoleRes4s> roleResList = new ArrayList<RoleRes4s>();
		if (StrKit.notBlank(resIds)) {
			String[] ress = resIds.split(",");
			
			for (String resId : ress) {
				RoleRes4s roleRes = new RoleRes4s();
				roleRes.setRoleId(info.getId());
				roleRes.setResId(Long.parseLong(resId));
				roleResList.add(roleRes);
			}
		}
		grant(info.getId(), roleResList);
	}

	/**
	 * 授权
	 * @param id
	 * @param roleResList
	 */
	public void grant(Long roleId, List<RoleRes4s> roleResList) {
		RoleRes4s.dao.deleteByRoleId(roleId);
		Db.batchSave(roleResList, roleResList.size());
		ShiroCacheUtils.clearAuthorizationInfoAll();
		ShiroUtils.refreshMenuByRoleId(roleId);
	}

	/**
	 * 更新角色
	 * @param info
	 * @param resIds
	 */
	@Before(Tx.class)
	public void updateRole(Role4s info, String resIds) {
		Role4s _info = Role4s.dao.findById(info.getId());
		if (_info == null) {
			throw new BusinessException("角色名称" + info.getName() + "不存在");
		}
		
		_info.setName(info.getName());
		_info.setDes(info.getDes());
		_info.setSeq(info.getSeq());
		_info.setStatus(info.getStatus());
		_info.setLastUpdAcct(info.getLastUpdAcct());
		_info.setLastUpdTime(info.getLastUpdTime());
		_info.setNote("更新角色");
		_info.update();
		
		List<RoleRes4s> roleResList = new ArrayList<RoleRes4s>();
		if (StrKit.notBlank(resIds)) {
			String[] ress = resIds.split(",");
			
			for (String resId : ress) {
				RoleRes4s roleRes = new RoleRes4s();
				roleRes.setRoleId(info.getId());
				roleRes.setResId(Long.parseLong(resId));
				roleResList.add(roleRes);
			}
		}
		grant(info.getId(), roleResList);
	}
}
