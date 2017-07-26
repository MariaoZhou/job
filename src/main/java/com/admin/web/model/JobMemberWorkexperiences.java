package com.admin.web.model;

import com.admin.web.model.base.BaseJobMemberWorkexperiences;

import java.util.List;

/**
 * 会员 > 工作经历
 */
@SuppressWarnings("serial")
public class JobMemberWorkexperiences extends BaseJobMemberWorkexperiences<JobMemberWorkexperiences> {
	public static final JobMemberWorkexperiences dao = new JobMemberWorkexperiences().dao();
	public static final String table = "job_member_workexperiences";

	/**
	 * 通过 会员id 查询 工作经验
	 * @param memberId
	 * @return
	 */
	public List<JobMemberWorkexperiences> findByMemberId (Integer memberId){
		return dao.find("select * from " + table + " where memberId = ?", memberId);
	}
}
