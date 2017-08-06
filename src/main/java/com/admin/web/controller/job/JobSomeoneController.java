package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.City;
import com.admin.web.model.JobData;
import com.admin.web.model.Someone;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

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

    @ApiOperation(description = " 发布找人办事" ,url = "/job/someone/publishSomeone", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "userId", description = "用户id 必填", dataType = "int"),
            @Param(name = "cityId", description = "城市id 必填", dataType = "int"),
            @Param(name = "someoneType", description = "分类 必填", dataType = "String"),
            @Param(name = "title", description = "标题 必填", dataType = "String"),
            @Param(name = "tel", description = "联系电话 必填", dataType = "String"),
            @Param(name = "details", description = "详情说明", dataType = "String")
    })
    public void publishSomeone(){
        Someone someone = new Someone();
        // 用户id
        String userId = getPara("userId");
        // 城市id
        String cityId = getPara("cityId");

        someone.setSomeoneTypeName(getPara("someoneType"));

        String title = getPara("title");
        someone.setTitle(title);
        String tel = getPara("tel");
        someone.setTel(tel);
        String details = getPara("details");
        someone.setDetails(details);

        boolean status = jobConfigService.saveSomeone(someone, cityId, userId);
        if (status){
            renderJson(R.ok());
        }else {
            renderJson(R.error());
        }
    }

    @ApiOperation(description = " 找人办事 条件搜索" ,url = "/job/someone/searchSomeone", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int"),
            @Param(name = "someoneType", description = "分类", dataType = "String"),
            @Param(name = "pageNumber", description = "页码 必填", dataType = "int")
    })
    public void searchSomeone(){
        Someone someone = new Someone();
        // 国家id
        Integer countriesId = getParaToInt("countriesId",0);
        someone.setCountriesId(countriesId);
        // 分类
        String someoneTypeName = getPara("someoneType");
        someone.setSomeoneTypeName(someoneTypeName);
        // 页码
        Integer pageNumber = getParaToInt("pageNumber",1);

        Page<Someone> map = jobConfigService.searchSomeone(someone, pageNumber);

        renderJson(R.ok().put(map));
    }


    @ApiOperation(description = " 获取 找人办事参数 + 找人办事搜索" ,url = "/job/someone/someoneConfig", tag = "JobSomeoneController", httpMethod = "get")
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

    @ApiOperation(description = " 找人办事 详情信息" ,url = "/job/someone/someoneInfo", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "someoneId", description = "办事id 必填", dataType = "int")
    })
    public void someoneInfo(){

        String someoneId = getPara("someoneId");

        Someone someone = Someone.dao.findById(someoneId);
        renderJson(R.ok().put(someone));
    }


	@Override
	public void onExceptionError(Exception e) {
        renderJson(R.error("系统异常, 请稍候重试"));
		
	}

}
