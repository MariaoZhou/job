package com.admin.web.service.job;


import com.admin.web.base.BaseBussinessService;
import com.admin.web.model.JobMember;
import com.admin.web.model.JobMemberPosition;
import com.admin.web.model.JobMessageWindow;
import com.admin.web.model.JobUnitPosition;
import com.admin.web.util.DateUtils;
import com.admin.web.util.excel.JobExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  招聘 服务
 * @author Rlax
 *
 */
public class JobService extends BaseBussinessService {

	/**
	 * 通过职位 查询 谁投递过这个职位
	 * @return
	 */
	@Before(Tx.class)
	public List<JobMember> jobMemeber(Integer jobId){
		String sql = "select * from job_member_position where jobId = ?";
		List<JobMemberPosition> memberPositions = JobMemberPosition.dao.find(sql, jobId);
		List<JobMember> jobMembers = new ArrayList<>();
		for (JobMemberPosition position : memberPositions){
			JobMember members =  JobMember.dao.findById(position.getMemberId());
			if (members!= null){
				jobMembers.add(members);
			}
		}

		return jobMembers;
	}

	/**
	 * 查询 招聘job 列表
	 * @param
	 * @return
	 */
	@Before(Tx.class)
	public JSONArray jobList(List<JobUnitPosition> positionList) {
		JSONArray jsonArray = new JSONArray();
		for (JobUnitPosition p : positionList ){
			JSONObject pp = new JSONObject();

			pp.put("jobId", p.getId());
			pp.put("job", p.getJob());
			pp.put("unit",p.getUnit());
			pp.put("country",  p.getCountry());
			pp.put("city", p.getCity());
			pp.put("street", p.getStreet());
			pp.put("salary", p.getSalary());
			pp.put("unitHead", p.getMemberImage());
			pp.put("unitUser", p.getMemberName());
			pp.put("createDate", DateUtils.formatDate(p.getCreateDate()));

			jsonArray.add(pp);
		}

		return jsonArray;
	}

	/**
	 * 通过职位详情查询 投递记录
	 * @param jobId
	 * @param memberId
	 * @return
	 */
	public JSONObject findMemberPositionStatus(Integer jobId, Integer memberId){
		JSONObject res = new JSONObject();
		JobUnitPosition job = JobUnitPosition.dao.findById(jobId);

		res.put("job", job.getJob());
		res.put("salary", job.getSalary());
		res.put("country", job.getCountry());
		res.put("city", job.getCity());
		res.put("street", job.getStreet());
		res.put("qualification", job.getQualification());
		res.put("strength", job.getStrength());
		res.put("tel", job.getTel());
		res.put("jobDetail", job.getJobDetail());
		res.put("skill", job.getSkill());
		res.put("userId", job.getMemberId());

		res.put("unitHead", job.getMemberImage());
		res.put("unit", job.getUnit());
		res.put("createDate", DateUtils.formatDate(job.getCreateDate()));

		// 职位 简历投递 控制 0未投递 1已投递
		JobMemberPosition memberPosition = JobMemberPosition.dao.findFirst("select * from job_member_position where " +
											"jobId = ? and memberId = ? ", jobId, memberId);

		if (memberPosition == null){
			res.put("status", "0");
		}else {
			res.put("status", "1");
		}

		// 消息窗口 控制 0 未创建会话 !0 会话id
		JobMessageWindow messageWindow = JobMessageWindow.dao.findFirst("select * from job_message_window where " +
										 "unitPositionId = ? and memberId = ?", jobId, memberId);

		if (messageWindow == null) {
			res.put("msgWindowId", 0);
		}else {
			res.put("msgWindowId", messageWindow.getId());
		}

		return res;
	}

	@Before(Tx.class)
	public String excelImport(String excelFile){
		ImportParams params = new ImportParams();
		List<JobExcel> list = ExcelImportUtil.importExcel(new File(excelFile), JobExcel.class, params);
		JobMember member = new JobMember();
		member.setId(null);
		member.setName(null);
		member.setImage(null);

		for (JobExcel job : list) {
			//System.out.println(job.toString());

			String sql = String.format(
					"INSERT INTO `job`.`job_unit_position` " +
							"(`job`, `salary`, `unit`, `country`, `city`, `skill`, `street`, `jobDetail`, `tel`, `qualification`, `strength`, `createDate`) VALUES " +
							"('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",

					job.getName(), job.getSalary(), job.getUnit(), job.getCountry(), job.getCity(),
					job.getSkill(), job.getStreet(), job.getJobDetail(), job.getTel(), job.getQualification(), job.getStrength(), DateUtils.getDateTime());


			System.out.println(sql);
		}

		return null;
	}


}
