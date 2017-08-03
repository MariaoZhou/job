package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.City;
import com.admin.web.model.JobData;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  找人办事 controller
 */
@Api(tag = "JobSomeoneController", description = "找人办事 接口")
@ControllerBind(controllerKey = "/job/someone")
public class JobSomeoneController extends BaseBussinessController {

    private static JobConfigService jobConfigService = JobConfigService.me;

    /**
     * 发布 职位
     */
    public void publishJob(){



       // renderJson(R.ok().put());
    }


    @ApiOperation(description = " 获取 找人办事 参数" ,url = "/job/someone/someoneConfig", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int")
    })
    public void someoneConfig(){
        Map<String, Object> map = new HashMap<>();

        String countries = getPara("countriesId");

        // 城市
        List<City> cityList = City.dao.find("select * from j_city where countriesId = ?" ,countries);
        // 找人办事 类型 值 SOMEONE_TYPE
        List<Map<String, String>> someoneType = JobData.dao.getDateListByType("SOMEONE_TYPE");

        map.put("cityList", cityList);
        map.put("someoneType", someoneType);

        renderJson(R.ok(map));
    }



	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
