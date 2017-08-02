package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;

/**
 *  职位 招聘 controller
 */
@ControllerBind(controllerKey = "/job/info")
public class JobInfoController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;

    /**
     * 发布 职位
     */
    public void publishJob(){



       // renderJson(R.ok().put());
    }

    /**
     * 发布 职位 参数
     */
    public void jobInfoConfig(){
        String countries = getPara("countriesId");
        renderJson(R.ok().put(jobConfigService.jobInfoConfig(countries)));
    }



	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
