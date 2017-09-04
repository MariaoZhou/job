package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.City;
import com.admin.web.model.JobData;
import com.admin.web.model.Someone;
import com.admin.web.model.UserInfo;
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

    @ApiOperation(description = " 发布找人办事（找活挣钱）" ,url = "/job/someone/publishSom", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "id", description = "id =null 添加, !=null 修改", dataType = "int"),
            @Param(name = "userId", description = "用户id 必填", dataType = "int"),
            @Param(name = "cityId", description = "城市id 必填", dataType = "int"),
            @Param(name = "companyName", description = "公司名称", dataType = "String"),
            @Param(name = "companyLogo", description = "公司Logo地址", dataType = "String"),
            @Param(name = "companyPublicity", description = "公司宣传图地址", dataType = "String"),
            @Param(name = "companyQRCode", description = "公司二维码地址", dataType = "String"),
            @Param(name = "companyInfo", description = "公司宣传语", dataType = "String"),
            @Param(name = "someoneType", description = "分类 必填", dataType = "String"),
            @Param(name = "title", description = "标题 必填", dataType = "String"),
            @Param(name = "tel", description = "联系电话 必填", dataType = "String"),
            @Param(name = "details", description = "详情说明", dataType = "String")
    })

    public void publishSom(){
        Someone someone = new Someone();

        Integer id = getParaToInt("id",null);
        someone.setId(id);

        // 用户id
        String userId = getPara("userId");
        UserInfo userInfo = UserInfo.dao.findById(userId);

        if (userInfo.getBlacklist().equals("1")){
            renderJson(R.error("您在黑名单中, 不能发布信息."));
            return;
        }

        someone.setUserId(userInfo.getId());
        someone.setUserName(userInfo.getName());
        // 城市id
        String cityId = getPara("cityId");

        someone.setSomeoneTypeName(getPara("someoneType"));

        // 公司信息
        String companyName = getPara("companyName");
        someone.setCompanyName(companyName);
        String companyLogo = getPara("companyLogo");
        someone.setCompanyLogo(companyLogo);
        String companyPublicity = getPara("companyPublicity");
        someone.setCompanyPublicity(companyPublicity);
        String companyQRCode = getPara("companyQRCode");
        someone.setCompanyQRCode(companyQRCode);
        String companyInfo = getPara("companyInfo");
        someone.setCompanyInfo(companyInfo);

        String title = getPara("title");
        someone.setTitle(title);
        String tel = getPara("tel");
        someone.setTel(tel);
        String details = getPara("details");
        someone.setDetails(details);

        boolean stu = jobConfigService.saveSomeone(someone, cityId, userId);
        System.out.println("sId ;;;" +someone.getId() );
        if (stu){
            renderJson(R.ok().put("someoneId", someone.getId()));
        }else {
            renderJson(R.error());
        }
    }


    @ApiOperation(description = " 找人办事 条件搜索 列表页" ,url = "/job/someone/searchSomeone", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int"),
            @Param(name = "userId", description = "当前用户id 必填", dataType = "int"),
            @Param(name = "someoneType", description = "分类 多选 逗号分隔", dataType = "String"),
            @Param(name = "title", description = "标题", dataType = "String"),
            @Param(name = "pageNumber", description = "页码 必填", dataType = "int"),
            @Param(name = "pageSize", description = "每页长度", dataType = "int")
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
        Integer pageSize = getParaToInt("pageSize", Consts.PAGE_DEFAULT_SIZE);
        // 标题
        String title = getPara("title");
        someone.setTitle(title);

        Integer userId = getParaToInt("userId");

        Page<Someone> map = jobConfigService.searchSomeone(someone, userId,pageNumber, pageSize);

        renderJson(R.ok().put(map));
    }


    @ApiOperation(description = " 获取 找人办事参数 + 找人办事搜索参数" ,url = "/job/someone/someoneConfig", tag = "JobSomeoneController", httpMethod = "get")
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

        renderJson(R.ok().put(map));
    }

    @ApiOperation(description = " 找人办事 详情信息" ,url = "/job/someone/someoneInfo", tag = "JobSomeoneController", httpMethod = "get")
    @Params({
            @Param(name = "someoneId", description = "办事id 必填", dataType = "int"),
            @Param(name = "userId", description = "用户id 选填", dataType = "int")
    })
    public void someoneInfo(){
        String userId = getPara("userId");
        String someoneId = getPara("someoneId");

        if (StrKit.notBlank(userId)){
            List<String> params = new ArrayList<>();
            params.add(userId);
            params.add(someoneId);
            String sql = "select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId from j_someone o " +
                    "LEFT JOIN user_collection c on o.id = c.jobId and c.type = '2' and c.userId = ? where o.id = ? ";

            Someone someone = Someone.dao.findFirst(sql,params.toArray());
            renderJson(R.ok().put(someone));
        }else {
            Someone someone = Someone.dao.findById(someoneId);
            renderJson(R.ok().put(someone));
        }
    }

    @Override
	public void onExceptionError(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        renderJson(R.error("系统异常, 请稍候重试"));

	}

}
