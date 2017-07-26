package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobMember;
import com.admin.web.model.JobMessageDetails;
import com.admin.web.model.JobMessageWindow;
import com.admin.web.model.JobUnitPosition;
import com.admin.web.util.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.rlax.web.model.Data;

import java.util.Date;
import java.util.List;

/**
 *  聊天会话 接口Controller
 * <p>Description: JobMessageController </p>
 * @author:  xutie
 * @created: 2017/6/15
 * @version: 1.0
 */
@ControllerBind(controllerKey = "/job/msg")
public class JobMessageController extends BaseBussinessController {

	/**
	 * 创建留言会话
	 */
	public void msgWindow(){

		JobMessageWindow mw = new JobMessageWindow();

		Integer memberId = getParaToInt("userId");
		JobMember member = JobMember.dao.findById(memberId);

		Integer unitUserId = getParaToInt("unitUserId");
		JobMember unitUser = JobMember.dao.findById(unitUserId);

		mw.setMemberId(memberId);
		mw.setMemberName(member.getName());
		mw.setMemberImage(member.getImage());

		mw.setUnitUserId(unitUserId);
		mw.setUnitUserImage(unitUser.getImage());
		mw.setUnitUserName(unitUser.getName());

		Integer jobId = getParaToInt("jobId");
		mw.setUnitPositionId(jobId);
		JobUnitPosition up = JobUnitPosition.dao.findById(jobId);
		mw.setUnit(up.getUnit());

		if (mw.save()){
			renderJson(R.ok().put("message",mw.getId()));
		}else {
			renderJson(R.error());
		}

	}


	/**
	 * 系统状态 1 0
	 */
	public void sysStatus (){

		String status = Data.dao.getCodeByCodeDescAndType("SYS_JOB_STATUS", "关闭");

		renderJson(R.ok().put(status));

	}

	/**
	 * 留言发送jf
	 *
	 */
	public void sendMsg(){

		JobMessageDetails details = new JobMessageDetails();
		details.setWindowId(getParaToInt("messageId"));
		details.setIdentity(getPara("identity"));	//身份(0应聘者1招聘者)
		details.setMessage(getPara("leaveWord"));
		details.setCreateDate(new Date());
		details.setMemberId(getParaToInt("userId"));
		details.setStatus("0");							//状态（0待阅1已阅）

		if (details.save()){
			renderJson(R.ok());
		}else {
			renderJson(R.error());
		}

	}

	/**
	 * 留言会话详情
	 */
	public void msgDetails(){

		Integer messageId = getParaToInt("messageId");
		JobMessageWindow mw = JobMessageWindow.dao.findById(messageId);

		List<JobMessageDetails> detailsList = JobMessageDetails.dao.find("select * from "+JobMessageDetails.table+ " where windowId="+mw.getId());

		JobUnitPosition job = JobUnitPosition.dao.findById(mw.getUnitPositionId());

		JSONObject res = new JSONObject();
		res.put("job", job.getJob());
		res.put("salary", job.getSalary());
		res.put("country", job.getCountry());
		res.put("city", job.getCity());
		res.put("street", job.getStreet());
		res.put("salary", job.getSalary());
		res.put("qualification", job.getQualification());
		res.put("unit", job.getUnit());
		res.put("messageId", mw.getId());
		res.put("unitHead", job.getMemberImage());
		res.put("userName", job.getMemberName());

		JSONArray msg = new JSONArray();

		for (JobMessageDetails md : detailsList){
			JSONObject mdObj = new JSONObject();

			mdObj.put("leaveWord", md.getMessage());
			mdObj.put("identity", md.getIdentity());
			mdObj.put("userId", md.getMemberId());
	/*		JobMember member = JobMember.dao.findById(md.getMemberId());

			mdObj.put("userName", member.getName());
			mdObj.put("userHead", member.getImage());*/

			msg.add(mdObj);
		}

		res.put("leaveWords",msg);

		renderJson(R.ok().put(res));

	}

	/**
	 * 留言列表
	 */
	public void messageList(){
		Integer memberId = getParaToInt("userId");

		List<JobMessageWindow> mwList = JobMessageWindow.dao.find("select * from " + JobMessageWindow.table +
										" where memberId = ? or unitUserId = ?", memberId, memberId);

		JSONArray yyArry = new JSONArray();
		JSONArray wyArry = new JSONArray();
		for (JobMessageWindow mw : mwList){
			JSONObject mwObj = new JSONObject();

			mwObj.put("messageId", mw.getId());
			mwObj.put("unitHead", mw.getUnitUserImage());
			mwObj.put("unitUser", mw.getUnitUserName());
			mwObj.put("unit", mw.getUnit());
			mwObj.put("messageId", mw.getId());
			mwObj.put("unitUserID", mw.getUnitUserId());

			String mdsql = "select * from job_message_details where windowId = ? ORDER BY createDate desc";
			List<JobMessageDetails> mdList = JobMessageDetails.dao.find(mdsql, mw.getId());

			if (mdList.size() > 0){
				JobMessageDetails md = mdList.get(0);

				mwObj.put("lastTime", md.getCreateDate());
				mwObj.put("lastMessage",  md.getMessage());
				mwObj.put("status",  md.getStatus());

				if ("1".equals(md.getStatus())){
					yyArry.add(mwObj);
				}else {
					wyArry.add(mwObj);
				}
			}
		}

		JSONObject object = new JSONObject();
		object.put("yy", yyArry);
		object.put("wy", wyArry);

		renderJson(R.ok().put(object));

	}

	/**
	 * 留言状态 更新
	 */
	public void updateMsgStatus(){

		Integer messageId = getParaToInt("messageId");
		Integer memberId = getParaToInt("userId");

		String sql = "update job_message_details set status = 1 where windowId = ? and memberId =?" ;

		Db.update(sql, messageId,memberId);

		renderJson(R.ok());

	}



	@Override
	public void onExceptionError(Exception e) {renderJson(R.error("接口调用异常"));}

}
