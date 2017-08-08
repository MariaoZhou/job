package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobInfo;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

/**
 *  职位 招聘 controller
 */
@Api(tag = "JobInfoController", description = "职位招聘 接口")
@ControllerBind(controllerKey = "/job/info")
public class JobInfoController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;

    @ApiOperation(description = "职位 条件搜索" ,url = "/job/info/searchJobInfo", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int"),
            @Param(name = "cityId", description = "城市id", dataType = "int"),
            @Param(name = "jobType", description = "工作类型", dataType = "String"),
            @Param(name = "jobNature", description = "工作性质", dataType = "String"),
            @Param(name = "type", description = "类型 =1（最高工资）=2 （企业查询） 最新发布(默认就会根据时间排序, 无需使用 type, 直接传递国家id 即可)", dataType = "String"),
            @Param(name = "pageNumber", description = "页码 必填", dataType = "int")
    })
    public void searchJobInfo(){
        JobInfo job = new JobInfo();
        // 国家id
        Integer countriesId = getParaToInt("countriesId",0);
        job.setCountriesId(countriesId);
        // 城市id
        Integer cityId = getParaToInt("cityId",0);
        job.setCityId(cityId);
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

        // 页码
        Integer pageNumber = getParaToInt("pageNumber",1);

        Page<JobInfo> map = jobConfigService.searchJobInfo(job, type, pageNumber);

        renderJson(R.ok().put(map));

    }


    @ApiOperation(description = "发布职位" ,url = "/job/info/publishJob", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "userId", description = "用户id 必填", dataType = "int"),
            @Param(name = "cityId", description = "城市id 必填", dataType = "int"),
            @Param(name = "companyName", description = "公司名称", dataType = "String"),
            @Param(name = "companyLogo", description = "公司Logo地址", dataType = "String"),
            @Param(name = "companyPublicity", description = "公司宣传图地址", dataType = "String"),
            @Param(name = "companyQRCode", description = "公司二维码地址", dataType = "String"),
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
        // 用户id
        String userId = getPara("userId");
        // 城市id
        String cityId = getPara("cityId");

        // 公司信息
        String companyName = getPara("companyName");
        job.setCountriesName(companyName);
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
        job.setTel(title);
        String tel = getPara("tel");
        job.setTel(tel);
        String details = getPara("details");
        job.setDetails(details);

        boolean status = jobConfigService.saveJobInfo(job, cityId, userId);
        if (status){
            renderJson(R.ok());
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

    @ApiOperation(description = " 职位 查看详情信息" ,url = "/job/info/jobInfo", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "jobId", description = "职位id 必填", dataType = "int")
    })
    public void someoneInfo(){

        String jobId = getPara("jobId");

        JobInfo jobInfo = JobInfo.dao.findById(jobId);
        renderJson(R.ok().put(jobInfo));
    }



	@Override
	public void onExceptionError(Exception e) {
        renderJson(R.error("系统异常, 请稍候重试"));
		
	}

}
