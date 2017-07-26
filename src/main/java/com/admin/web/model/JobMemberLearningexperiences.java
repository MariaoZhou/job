package com.admin.web.model;

import com.admin.web.model.base.BaseJobMemberLearningexperiences;

import java.util.List;

/**
 * 会员 >教育经历
 */
@SuppressWarnings("serial")
public class JobMemberLearningexperiences extends BaseJobMemberLearningexperiences<JobMemberLearningexperiences> {
	public static final JobMemberLearningexperiences dao = new JobMemberLearningexperiences().dao();
	public static final String table = "job_member_learningexperiences";

	/**
	 * 通过 会员id 查询 教育经历
	 * @param memberId
	 * @return
	 */
	public List<JobMemberLearningexperiences> findByMemberId (Integer memberId){
		return dao.find("select * from " + table + " where memberId = ?", memberId);
	}
}
