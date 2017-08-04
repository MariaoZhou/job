package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.base.BaseCountries;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;

/**
 *  招聘 首页 controller
 */
@Api(tag = "JobIndexController", description = "招聘系统 接口")
@ControllerBind(controllerKey = "/job/index")
public class JobIndexController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;


    @ApiOperation(description = "获取位置" ,url = "/job/index/getLocation", tag = "JobIndexController", httpMethod = "get", response = BaseCountries.class)
    public void getLocation(){
        renderJson(R.ok().put(jobConfigService.getLocation()));
    }


    @ApiOperation(description = "获取 职位 搜索 参数" ,url = "/job/index/searchJobConfig", tag = "JobIndexController", httpMethod = "get", response = BaseCountries.class)
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int")
    })
    public void searchJobConfig(){

        String countries = getPara("countriesId");
        renderJson(R.ok().put(jobConfigService.searchJobConfig(countries)));
    }

	@Override
	public void onExceptionError(Exception e) {
        renderJson(R.error("系统异常, 请稍候重试"));
		
	}

}
