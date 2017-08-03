package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;

/**
 *  职位 招聘 controller
 */
@Api(tag = "JobInfoController", description = "职位招聘 接口")
@ControllerBind(controllerKey = "/job/info")
public class JobInfoController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;

    /**
     * 发布 职位
     */
    public void publishJob(){



       // renderJson(R.ok().put());
    }


    @ApiOperation(description = " 获取 发布职位 参数" ,url = "/job/info/jobInfoConfig", tag = "JobInfoController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int")
    })
    public void jobInfoConfig(){
        String countries = getPara("countriesId");
        renderJson(R.ok().put(jobConfigService.jobInfoConfig(countries)));
    }



	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
