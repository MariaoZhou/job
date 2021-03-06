package com.admin.web.controller.job;

import app.App;
import com.admin.web.base.BaseBussinessController;
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
                image.setUrlPath(App.APP_URL + "/job_file/"+time + uploadFile.getFileName());
                image.save();
                renderJson(R.ok().put("urlPath", image.getUrlPath()));
            }else {
                renderJson(R.error("文件复制失败"));
            }

        }else {
            renderJson(R.error("上传失败"));
        }
    }

	@Override
	public void onExceptionError(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        renderJson(R.error("系统异常, 请稍候重试"));
	}

}
