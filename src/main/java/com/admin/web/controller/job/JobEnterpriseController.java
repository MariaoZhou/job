package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobMember;
import com.admin.web.model.JobMemberLearningexperiences;
import com.admin.web.model.JobMemberWorkexperiences;
import com.admin.web.model.JobUnitPosition;
import com.admin.web.service.job.JobService;
import com.admin.web.util.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;

import java.util.Date;
import java.util.List;

/**
 *  企业版 接口Controller
 * <p>Description: JobController </p>
 * @author:  xutie
 * @created: 2017/6/15
 * @version: 1.0
 */
@ControllerBind(controllerKey = "/job/ent")
public class JobEnterpriseController extends BaseBussinessController {

    private JobService jobService = Duang.duang(JobService.class);

    /**
     * 我的 (企业版)
     * 调用参数 userId
     * 返回参数 如下
     {
     "unitHead":"./head.png",
     "unitUser":"徐析",
     "unitUserId":"123",
     "unit": "地图无忧"
     }
     */
    public void unitUser(){

        Integer memberId = getParaToInt("userId");
        JobMember member = JobMember.dao.findById(memberId);

        JSONObject res = new JSONObject();
        res.put("unitHead", member.getImage());
        res.put("unitUser", member.getName());
        res.put("unitUserId", member.getId());
        res.put("unit",member.getUnit());

        renderJson(R.ok().put(res));

    }

    /**
     * 通过职位 查询 谁投递过这个职位
     */
    public void jobMember(){
        Integer jobId = getParaToInt("jobId", 0);

        JobService jobService = Duang.duang(JobService.class);
        List<JobMember> jobMembers = jobService.jobMemeber(jobId);
        renderJson(R.ok().put(jobMembers));
    }

    /**
     * 添加/发布 招聘职位
     */
    public void recruit () {

        Integer memberId = getParaToInt("userId",1);
        JobUnitPosition position = new JobUnitPosition();

        // 用户信息
/*        JobMember member = JobMember.dao.findById(memberId);
        position.setMemberId(memberId);
        position.setMemberImage(member.getImage());
        position.setMemberName(member.getName());*/

        position.setId(getParaToInt("id",null));
        position.setUnit(getPara("unit"));
        position.setCountry(getPara("country"));
        position.setJob(getPara("job"));
        position.setCity(getPara("city"));
        position.setStreet(getPara("street"));
        position.setSalary(getPara("salary"));
        position.setQualification(getPara("qualification"));
        position.setTel(getPara("tel"));
        position.setStrength(getPara("strength"));
        position.setJobDetail(getPara("jobDetail"));
        position.setSkill(getPara("skill"));

        position.setCreateDate(new Date());

        try {
            if (position.getId()== null){
                position.save();
            }else {
                position.update();
            }

            renderJson(R.ok().put("jobId", position.getId()));
        }catch (Exception e){
            renderJson(R.error());
        }

    }

    /**
     * 查看简历
     */
    public void scanVN (){

        JSONObject res = new JSONObject();
        Integer memberId = getParaToInt("userId");
        JobMember member = JobMember.dao.findById(memberId);    //会员信息

        // 基础信息
        res.put("userHead", member.getImage());
        res.put("userId", member.getId());
        res.put("userName", member.getName());
        res.put("userStatus", member.getJobStatus());        // 应聘者状态（0离职1在职）
        res.put("sex", member.getSex());
        res.put("tel", member.getMobile());

        // 工作经验
        List<JobMemberWorkexperiences> works = JobMemberWorkexperiences.dao.findByMemberId(memberId);
        JSONArray wArray = new JSONArray();
        int x = 1;
        for (JobMemberWorkexperiences work : works){
            JSONObject wJson = new JSONObject();
            wJson.put("unit",work.getUnit());
            wJson.put("id",work.getId());
            wJson.put("positon",work.getPositon());
            wJson.put("jobContent",work.getJobContent());
            wJson.put("workstart",work.getWorkStart());
            wJson.put("workEnd",work.getWorkEnd());
            wArray.add(wJson);
            if (x == works.size()){
                res.put("workLastUnit", work.getUnit());
                res.put("workLastPositon", work.getUnit());
            }
        }
        res.put("workExperiences",wArray);

        // 教育经历
        List<JobMemberLearningexperiences> learningexperienceList = JobMemberLearningexperiences.dao.findByMemberId(memberId);
        JSONArray lArray = new JSONArray();
        for (JobMemberLearningexperiences ll : learningexperienceList){
            JSONObject lJson = new JSONObject();
            lJson.put("school", ll.getSchool());
            lJson.put("id", ll.getId());
            lJson.put("specialty", ll.getSpecialty());
            lJson.put("qualification", ll.getQualification());
            lJson.put("learingStart", ll.getLearingStart());
            lJson.put("learnEnd", ll.getLearnEnd());
            lArray.add(lJson);
        }
        res.put("learningExperiences", lArray);

        renderJson(R.ok().put(res));
    }


    /**
     * 招聘详细信息
     *
     * {
     "job": "高级web前端研发工程师",
     "salary": "15K-20K",
     "country": "西班牙",
     "city": "马德里",
     "street": "皇后大街",
     "qualification": "本科",
     "strength": "带薪年假|扁平管理",
     "unitHead": "./head.png",
     "unit": "地图无忧",
     "tel":"1854567800",
     "jobDetail": "米其林三星大厨",
     "skill": "做饭好吃"
     }
     */
    public void jobDetail(){

        Integer jobId = getParaToInt("jobId");
        Integer memberId = getParaToInt("userId", 0);

        JobService jobService = Duang.duang(JobService.class);

        try {
            JSONObject res = jobService.findMemberPositionStatus(jobId, memberId);        //招聘 详情
            if (StrKit.isBlank(res.getString("job"))){
                renderJson(R.error("查询失败!"));
            }else {
                renderJson(R.ok().put(res));
            }

        }catch (Exception e){
            System.out.println("e = " + e);
            renderJson(R.error("查询失败"));
        }

    }

    /**
     * 我发布的职位列表
     *
     */
    public void myJobList (){
        Integer userId = getParaToInt("userId", 0);
        List<JobUnitPosition> positions = JobUnitPosition.dao.find("select * from " + JobUnitPosition.table + " where " +
                                            "memberId = ? order by createDate desc" , userId);

        renderJson(R.ok().put(jobService.jobList(positions)));
    }

    /**
     * 招聘列表
     * 参数 search
     * search = null 查询所有
     * != null  job模糊查询
     */
    public void jobList(){
        String job = getPara("job",null);
        String country = getPara("country",null);
        String sql = "";
        if (StrKit.notBlank(job)){
            sql = "and instr(job, '"+job+"') > 0 ";
        }
        if (StrKit.notBlank(country)){
            sql = "and instr(country, '"+country+"') > 0 ";
        }
        List<JobUnitPosition> positionList = JobUnitPosition.dao.find("select * from " + JobUnitPosition.table + " where 1=1 " + sql + " order by createDate desc");
        renderJson(R.ok().put(jobService.jobList(positionList)));
    }

    /**
     * 我投递过的职位列表
     */
    public void mySendJobList(){
        Integer userId = getParaToInt("userId",0);

        String jobSql = "SELECT up.*, mp.status FROM job_unit_position up " +
                "LEFT JOIN job_member_position mp ON up.id = mp.jobId WHERE mp.memberId =? order by up.createDate desc";

        List<JobUnitPosition> positionList = JobUnitPosition.dao.find(jobSql, userId);
        renderJson(R.ok().put(jobService.jobList(positionList)));

    }

    /**
     * 企业查看过我简历的职位列表
     */
    public void myJobUnitShow(){
        Integer userId = getParaToInt("userId",0);

        String jobSql = "SELECT up.*, mp.status FROM job_unit_position up " +
                "LEFT JOIN job_member_position mp ON up.id = mp.jobId WHERE mp.memberId =? " +
                "and mp.status = '1' order by up.createDate desc";

        List<JobUnitPosition> positionList = JobUnitPosition.dao.find(jobSql, userId);
        renderJson(R.ok().put(jobService.jobList(positionList)));
    }
	
	@Override
	public void onExceptionError(Exception e) {renderJson(R.error("接口调用异常"));}

}
