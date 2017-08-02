package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobData;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;

import java.util.List;
import java.util.Map;

/**
 *  招聘 首页 controller
 */
@ControllerBind(controllerKey = "/job/index")
public class JobIndexController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;

    /**
     * 获取位置
     */
    public void getLocation(){
        renderJson(R.ok().put(jobConfigService.getLocation()));
    }

    /**
     * 获取 搜索 职位 参数
     */
    public void searchJobConfig(){

        String countries = getPara("countriesId");
        renderJson(R.ok().put(jobConfigService.searchJobConfig(countries)));
    }

    /**
     * 搜索 找人办事 参数
     */
    public void searchSomeoneConfig(){

        String countries = getPara("countriesId");
        // 找人办事 类型 值 SOMEONE_TYPE
        List<Map<String, String>> someoneType = JobData.dao.getDateListByType("SOMEONE_TYPE");

        renderJson(R.ok().put("someoneType",someoneType));
    }


	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
