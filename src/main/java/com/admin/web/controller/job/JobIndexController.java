package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.Someone;
import com.admin.web.model.base.BaseCountries;
import com.admin.web.service.job.JobConfigService;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.DateUtils;
import com.admin.web.util.FileUtils;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.Date;

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

    @ApiOperation(description = "获取默认列表 职位and找人办事" ,url = "/job/index/searchJobIndex", tag = "JobIndexController", httpMethod = "get", response = BaseCountries.class)
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int"),
            @Param(name = "userId", description = "当前用户id 必填", dataType = "int")
    })
    public void searchJobIndex(){

        String countries = getPara("countriesId");
        Integer userId = getParaToInt("userId");

        renderJson(R.ok().put(jobConfigService.searchJobIndex(countries,userId)));
    }


    @ApiOperation(description = "获取 职位 搜索 参数" ,url = "/job/index/searchJobConfig", tag = "JobIndexController", httpMethod = "get", response = BaseCountries.class)
    @Params({
            @Param(name = "countriesId", description = "国家id 必填", dataType = "int")
    })
    public void searchJobConfig(){

        String countries = getPara("countriesId");
        renderJson(R.ok().put(jobConfigService.searchJobConfig(countries)));
    }

    @ApiOperation(description = "图片上传 接口" ,url = "/job/index/uploadImage", tag = "JobIndexController", httpMethod = "get", response = BaseCountries.class)
    public void uploadImage(){
        String time = DateUtils.formatDate(new Date(), "yyMMdd") + "/";
        String imagePath = "/home/www/job_file/"+time;      //图片实际存储位置
        UploadFile uploadFile = getFile();
        
        if (uploadFile!=null){

            com.admin.web.model.UploadFile image = new com.admin.web.model.UploadFile();
            image.setName(uploadFile.getFileName());
            image.setUploadPath(imagePath+uploadFile.getFileName());
            image.setType(uploadFile.getContentType());

            if (FileUtils.copyFile(uploadFile.getUploadPath()+File.separator+uploadFile.getFileName(), imagePath+uploadFile.getFileName())){
                FileUtils.deleteFile(uploadFile.getUploadPath()+File.separator+uploadFile.getFileName());
                image.setUrlPath(PropKit.get("app.url") + "/job_file/"+time + uploadFile.getFileName());
                image.save();
                renderJson(R.ok().put("urlPath", image.getUrlPath()));
            }else {
                renderJson(R.error("文件复制失败"));
            }

        }else {
            renderJson(R.error("上传失败"));
        }

    }

    @ApiOperation(description = "发布找人办事" ,url = "/job/index/publishSom", tag = "JobIndexController", httpMethod = "get")
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

        someone.save();
        renderJson(R.ok());
        boolean stu = jobConfigService.saveSomeone(someone, cityId, userId);
        System.out.println("sId ;;;" +someone.getId() );
        if (stu){
            renderJson(R.ok().put("someone", someone.getId()));
        }else {
            renderJson(R.error());
        }
    }
	@Override
	public void onExceptionError(Exception e) {
        renderJson(R.error("系统异常, 请稍候重试"));
		
	}

}
