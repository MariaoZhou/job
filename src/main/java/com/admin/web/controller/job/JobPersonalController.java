package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobMember;
import com.admin.web.model.JobMemberLearningexperiences;
import com.admin.web.model.JobMemberPosition;
import com.admin.web.model.JobMemberWorkexperiences;
import com.admin.web.util.DateUtils;
import com.admin.web.util.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;

import java.util.Date;
import java.util.List;

/**
 *  个人版 接口Controller
 * <p>Description: JobController </p>
 * @author:  xutie
 * @created: 2017/6/15
 * @version: 1.0
 */
@ControllerBind(controllerKey = "/job/per")
public class JobPersonalController extends BaseBussinessController {

    /**
     * 发送简历
     */
    public void sendJob(){

        Integer jobId = getParaToInt("jobId");
        Integer userId = getParaToInt("userId");

        JobMemberPosition mp = new JobMemberPosition();
        mp.setJobId(jobId);
        mp.setMemberId(userId);
        // 状态默认 未阅0
        mp.setStatus("0");

        mp.save();
        renderJson(R.ok());

    }

    /**
     * 投递状态更新为已阅
     */
    public void updateJob (){
        Integer jobId = getParaToInt("jobId");
        Integer userId = getParaToInt("userId");

        JobMemberPosition mp = JobMemberPosition.dao.findFirst("select * from job_member_position where jobId = ? and memberId = ?",
                                jobId,userId);

        if (mp!=null){
            mp.setStatus("1");
            mp.update();
            renderJson(R.ok());
        }else {
            renderJson(R.error("未查询到该投递记录"));
        }

    }


    /**
     * 我的 (个人版)
     * 调用参数 userId
     * 返回参数 如下
     userStatus应聘者状态（0离职1在职）
    {
        "userHead":"./head.png",
            "userName":"田野",
            "userId":"123",
            "userStatus":"0"
    }*/
    public void user(){

        Integer memberId = getParaToInt("userId");
        JobMember member = JobMember.dao.findById(memberId);

        JSONObject res = new JSONObject();
        res.put("userHead", member.getImage());
        res.put("userName", member.getName());
        res.put("userId", member.getId());
        res.put("userStatus", member.getJobStatus());

        renderJson(R.ok().put(res));

    }

    /**
     * 用户信息更新 (头像, 身份)
     */
    public void userUpdate(){
        Integer memberId = getParaToInt("userId");

        String memberHead = getPara("userHead", null);
        String memberRole = getPara("role", null);

        JobMember member = new JobMember();
        member.setId(memberId);
        if (StrKit.notBlank(memberHead)){
            member.setImage(memberHead);
        }
        if (StrKit.notBlank(memberRole)){
            member.setRole(memberRole);
        }

        if (member.update()){
            renderJson(R.ok());
        }else {
            renderJson(R.error());
        }

    }

    /**
     * 删除 工作经验或教育信息
     */
    public void userDeltele(){

        Integer id = getParaToInt("id");
        String type = getPara("type");

        try {
            if (type.equals("work")){
                JobMemberWorkexperiences.dao.deleteById(id);
            }else if (type.equals("learning")){
                JobMemberLearningexperiences.dao.deleteById(id);
            }else {
                renderJson(R.error("请确定参数是否正确"));
            }
        }catch (Exception e){
            renderJson(R.error());
        }
        renderJson(R.ok());
    }

    /**
     * 我的 > 个人信息
     */
    public void myMessage(){

        JobMember mm = new JobMember();

        Integer memberId = getParaToInt("userId");
        String status = getPara("status","0");

        // status 0查询 1更新
        if (status.equals("0")){

            renderJson(R.ok().put(mm.findById(memberId)));

        }else if ((status.equals("1"))){
            mm.setId(getParaToInt("userId"));
            mm.setName(getPara("userName"));
            mm.setMobile(getPara("tel"));
            mm.setSex(getPara("sex","0"));
            mm.setBeginWork(getPara("beginWork"));
            mm.setJobStatus(getPara("workStatus")); //工作状态
            mm.setResidencePermit(getPara("residencePermit"));
            mm.setBranchOfWork(getPara("branchOfWork"));
            mm.setBirthday(getPara("birthday"));
            if (mm.update()){
                renderJson(R.ok());
            }else {
                renderJson(R.error());
            }
        }
    }

    /**
     * 查看 我的简历 (个人版)
     */

    public void CV (){

        JSONObject res = new JSONObject();
        Integer memberId = getParaToInt("userId");
        JobMember member = JobMember.dao.findById(memberId);    //会员信息

        // 基础信息
        res.put("userHead", member.getImage());
        res.put("userId", member.getId());
        res.put("userName", member.getName());
        res.put("sex", member.getSex());

        // 工作经验
        List<JobMemberWorkexperiences> works = JobMemberWorkexperiences.dao.findByMemberId(memberId);
        JSONArray wArray = new JSONArray();
        for (JobMemberWorkexperiences work : works){
            JSONObject wJson = new JSONObject();
            wJson.put("unit",work.getUnit());
            wJson.put("id",work.getId());
            if (work.getWorkStart() != null){
                wJson.put("workstart",work.getWorkStart());
            }
            if (work.getWorkEnd() != null){
                wJson.put("workEnd",work.getWorkEnd());
            }

            wArray.add(wJson);
        }
        res.put("workExperiences",wArray);

        // 教育经历
        List<JobMemberLearningexperiences> learningexperienceList = JobMemberLearningexperiences.dao.findByMemberId(memberId);
        JSONArray lArray = new JSONArray();
        for (JobMemberLearningexperiences ll : learningexperienceList){
            JSONObject lJson = new JSONObject();
            lJson.put("school", ll.getSchool());
            lJson.put("id", ll.getId());
            lJson.put("learingStart", ll.getLearingStart());
            lJson.put("learnEnd", ll.getLearnEnd());
            lJson.put("specialty", ll.getSpecialty());
            lJson.put("qualification", ll.getQualification());
            lJson.put("learningExperience", ll.getLearningExperience());

            lArray.add(lJson);
        }
        res.put("learningExperiences", lArray);

        renderJson(R.ok().put(res));
    }

    /**
     * 添加|修改 教育经历
     */
    public void learningExperience(){
        Integer memberId = getParaToInt("userId");
        String status = getPara("status","0");

        JobMemberLearningexperiences lp = new JobMemberLearningexperiences();

        if (status.equals("0")){
            lp = lp.findFirst("select * from job_member_learningexperiences where memberId = ?" , memberId);

            renderJson(R.ok().put(lp));
        }else {
            lp.setId(getParaToInt("id",0));
            lp.setMemberId(memberId);
            lp.setSchool(getPara("school"));
            lp.setSpecialty(getPara("specialty"));
            lp.setQualification(getPara("qualification"));
            lp.setLearningExperience(getPara("learningExperience"));
            lp.setLearingStart(getPara("learingStart"));
            lp.setLearnEnd(getPara("learnEnd"));

            System.out.println("lp.toString() = " + lp.toString());

            if (lp.getId() == 0) {
                lp.save();
            }else {
                lp.update();
            }
            renderJson(R.ok());
        }

    }

    /**
     * 添加/修改 工作经历
     */
    public void workExperience(){
        Integer memberId = getParaToInt("userId");
        JobMemberWorkexperiences wr = new JobMemberWorkexperiences();
        String status = getPara("status","0");

        if (status.equals("0")){
            wr = wr.findFirst("select * from job_member_workexperiences where memberId = ?", memberId);
            JSONObject wrJson = (JSONObject) JSON.toJSON(wr);

            if (wr.getWorkStart() != null){
                wrJson.put("workStart", wr.getWorkStart());
            }
            if (wr.getWorkEnd() != null){
                wrJson.put("workEnd",wr.getWorkEnd());
            }
            renderJson(R.ok().put(wrJson));
        }else {
            wr.setId(getParaToInt("id"));
            wr.setMemberId(memberId);
            wr.setUnit(getPara("unit"));
            wr.setPositon(getPara("positon"));
            wr.setJobContent(getPara("jobContent"));
            wr.setWorkStart(getPara("workstart"));
            wr.setWorkEnd(getPara("workEnd"));

            try {
                if (wr.getId() == null ) {
                    wr.save();
                }else {
                    wr.update();
                }
                renderJson(R.ok());
            }catch (Exception e){
                renderJson(R.error());
            }

        }


    }
	
	@Override
	public void onExceptionError(Exception e) {renderJson(R.error("接口调用异常"));}

}
