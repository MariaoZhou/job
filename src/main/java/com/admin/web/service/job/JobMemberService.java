package com.admin.web.service.job;


import com.admin.web.base.BaseBussinessService;
import com.admin.web.model.JobMember;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 招聘 会员
 * @author Rlax
 *
 */
public class JobMemberService extends BaseBussinessService {

	/**
	 * 保存用户
	 * @param member
	 */
	@Before(Tx.class)
	public JobMember saveAndUpdateMember(JobMember member) {

		JobMember jobMember = JobMember.dao.findByOpenId(member.getOpenId());

		if (jobMember == null){

			member.save();
		}else {
			member = jobMember;
		}

		return member;
	}


}
