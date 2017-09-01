package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobInfo;
import com.admin.web.model.Someone;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.Consts;

import java.util.ArrayList;
import java.util.List;

/**
 *  职位 招聘 controller
 */
@Api(tag = "JobInfoController", description = "职位招聘 接口")
@ControllerBind(controllerKey = "/job/info")
public class JobInfoController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;

    @ApiOperation(description = "职位 条件搜索 列表页" ,url = "/job/info/searchJobInfo", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int"),
            @Param(name = "userId", description = "当前用户Id 必填", dataType = "int"),
            @Param(name = "cityName", description = "城市名称 多选 逗号分隔", dataType = "int"),
            @Param(name = "jobType", description = "工作类型 多选 逗号分隔", dataType = "String"),
            @Param(name = "title", description = "标题", dataType = "String"),
            @Param(name = "jobNature", description = "工作性质 多选 逗号分隔", dataType = "String"),
            @Param(name = "type", description = "类型 =1（最高工资）=2 （企业查询） 最新发布(默认就会根据时间排序, 无需使用 type, 直接传递国家id 即可)", dataType = "String"),
            @Param(name = "pageNumber", description = "页码 必填", dataType = "int"),
            @Param(name = "pageSize", description = "每页长度", dataType = "int")
    })
    public void searchJobInfo(){
        JobInfo job = new JobInfo();
        // 国家id
        Integer countriesId = getParaToInt("countriesId",0);
        job.setCountriesId(countriesId);
        // 城市名称
        String cityName = getPara("cityName");
        job.setCityName(cityName);
        // 工作类型
        String jobType = getPara("jobType");
        job.setJobTypeName(jobType);
        // 工作性质
        String jobNature = getPara("jobNature");
        job.setJobNatureName(jobNature);
        // 最高工资1
        // 最新发布2
        // 企业查询3
        String type = getPara("type");
        // 标题
        String title = getPara("title");
        job.setTitle(title);

        Integer userId = getParaToInt("userId");

        // 页码
        Integer pageNumber = getParaToInt("pageNumber",1);
        Integer pageSize = getParaToInt("pageSize", Consts.PAGE_DEFAULT_SIZE);


        Page<JobInfo> map = jobConfigService.searchJobInfo(job, type, userId, pageNumber,pageSize);

        renderJson(R.ok().put(map));

    }


    @ApiOperation(description = "发布职位" ,url = "/job/info/publishJob", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "id", description = "id =null 添加, !=null 修改", dataType = "int"),
            @Param(name = "userId", description = "用户id 必填", dataType = "int"),
            @Param(name = "cityId", description = "城市id 必填", dataType = "int"),
            @Param(name = "companyName", description = "公司名称", dataType = "String"),
            @Param(name = "companyLogo", description = "公司Logo地址", dataType = "String"),
            @Param(name = "companyPublicity", description = "公司宣传图地址", dataType = "String"),
            @Param(name = "companyQRCode", description = "公司二维码地址", dataType = "String"),
            @Param(name = "companyInfo", description = "公司宣传语", dataType = "String"),
            @Param(name = "jobType", description = "工作种类 必填", dataType = "String"),
            @Param(name = "jobNature", description = "工作性质 必填", dataType = "String"),
            @Param(name = "jobWelfare", description = "福利待遇 必填", dataType = "String"),
            @Param(name = "jobSalary", description = "薪资区间 必填", dataType = "String"),
            @Param(name = "jobRequirements", description = "居住要求 必填", dataType = "String"),
            @Param(name = "title", description = "招聘标题 必填", dataType = "String"),
            @Param(name = "tel", description = "联系电话 必填", dataType = "String"),
            @Param(name = "details", description = "详细工作要求", dataType = "String")
    })
    public void publishJob(){

        JobInfo job = new JobInfo();

        Integer id = getParaToInt("id",null);
        job.setId(id);
        // 用户id
        String userId = getPara("userId");
        // 城市id
        String cityId = getPara("cityId");

        // 公司信息
        String companyName = getPara("companyName");
        job.setCompanyName(companyName);
        String companyLogo = getPara("companyLogo");
        job.setCompanyLogo(companyLogo);
        String companyPublicity = getPara("companyPublicity");
        job.setCompanyPublicity(companyPublicity);
        String companyQRCode = getPara("companyQRCode");
        job.setCompanyQRCode(companyQRCode);
        String companyInfo = getPara("companyInfo");
        job.setCompanyInfo(companyInfo);

        // 职位参数
        String jobTypeName = getPara("jobType");
        job.setJobTypeName(jobTypeName);
        String jobNatureName = getPara("jobNature");
        job.setJobNatureName(jobNatureName);
        String jobWelfareName = getPara("jobWelfare");
        job.setJobWelfareName(jobWelfareName);
        String jobSalaryName = getPara("jobSalary");
        job.setJobSalaryName(jobSalaryName);
        String jobRequirementsName = getPara("jobRequirements");
        job.setJobRequirementsName(jobRequirementsName);

        // 职位信息
        String title = getPara("title");
        job.setTitle(title);
        String tel = getPara("tel");
        job.setTel(tel);
        String details = getPara("details");
        job.setDetails(details);

        boolean status = jobConfigService.saveJobInfo(job, cityId, userId);
        if (status){
            renderJson(R.ok().put("jobId", job.getId()));
        }else {
            renderJson(R.error());
        }
    }


    @ApiOperation(description = " 获取 发布职位 参数" ,url = "/job/info/jobInfoConfig", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int")
    })
    public void jobInfoConfig(){
        String countries = getPara("countriesId");
        renderJson(R.ok().put(jobConfigService.jobInfoConfig(countries)));
    }

    @ApiOperation(description = " 职位 查看详情信息" ,url = "/job/info/jobInfoDetails", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "jobId", description = "职位id 必填", dataType = "int"),
            @Param(name = "userId", description = "用户id 选填", dataType = "int")
    })
    public void jobInfoDetails(){

        String userId = getPara("userId");
        String jobId = getPara("jobId");

        if (StrKit.notBlank(userId)){
            List<String> params = new ArrayList<>();
            params.add(userId);
            params.add(jobId);

            String sql = "select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId from j_job_info o " +
                    "LEFT JOIN user_collection c on o.id = c.jobId and c.type = '1' and c.userId = ? where o.id = ? ";

            JobInfo jobInfo = JobInfo.dao.findFirst(sql,params.toArray());
            renderJson(R.ok().put(jobInfo));
        }else {
            JobInfo jobInfo = JobInfo.dao.findById(jobId);
            renderJson(R.ok().put(jobInfo));
        }
    }

	@Override
	public void onExceptionError(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        renderJson(R.error("系统异常, 请稍候重试"));
		
	}

}
