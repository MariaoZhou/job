package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.City;
import com.admin.web.model.JobInfo;
import com.admin.web.model.UserInfo;
import com.admin.web.util.R;
import com.admin.web.util.excel.JobExcel;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.rlax.framework.common.Consts;
import com.rlax.web.model.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 职位 管理
 */
@ControllerBind(controllerKey = "/job/pc")
public class JobSettingController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
        JobInfo info = new JobInfo();
        info.setTitle(getPara("title"));
        info.setCityId(getParaToInt("cityId"));

		Page<JobInfo> list = JobInfo.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}

	public void delete(){
        String id = getPara("id");
        if (id.contains(",")){
            String[] ids = id.split(",");
            for (String ii :ids){
                JobInfo info = JobInfo.dao.findById(ii);
                info.delete();
            }
        }else {
            JobInfo info = JobInfo.dao.findById(id);
            info.delete();
        }

        setSessionSuccessMessage("操作成功");
        redirect("/job/pc");
    }

    /**
     * 跳转
     */
	public void excel(){
        render("excel.html");
	}

    /**
     * 下载
     */
	public void excelDownload(){

	    List<JobInfo> lists = JobInfo.dao.find("select * from j_job_info") ;

	    List<JobExcel> jobExcels = new ArrayList<>();
	    // String userId, String title, String companyName, String tel, String countriesName, String cityName, String jobTypeName, String jobNatureName, String jobWelfareName, String jobSalaryName, String jobRequirementsName, String details
	    for (JobInfo info : lists){
            JobExcel job = new JobExcel(
                    info.getUserId().toString(), info.getTitle(), info.getCompanyName(), info.getTel(), info.getCountriesName(), info.getCityName(),
                    info.getJobTypeName(), info.getJobNatureName(), info.getJobWelfareName(), info.getJobSalaryName(),
                    info.getJobRequirementsName(), info.getDetails()
            );

            jobExcels.add(job);
        }

        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);

        Workbook workbook = ExcelExportUtil.exportExcel(params , JobExcel.class, jobExcels);
        File savefile = new File(PropKit.get("app.downloads.basedir"));
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = null;
        String excelName = PropKit.get("app.downloads.basedir")+ "/"+ new Date().getTime()+".xlsx";
        try {
            fos = new FileOutputStream(excelName);
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File execlFile = new File(excelName);

        if (execlFile.exists()){
            renderFile(execlFile);
        }

    }

    /**
     * 导入
     */
	public void excelImport(){

        UploadFile uploadFile = getFile();

        String fileName = uploadFile.getOriginalFileName();
        File file = uploadFile.getFile();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        if (fileType.equals("xlsx")){
            ImportParams params = new ImportParams();
            List<JobExcel> list = ExcelImportUtil.importExcel(file, JobExcel.class, params);

            for (JobExcel job : list){
                UserInfo user = null;
                City city = null;
                try {
                    user = UserInfo.dao.findById(job.getUserId());
                    city = City.dao.findFirst("select * from j_city where name = ?" , job.getCityName());
                }catch (Exception e){
                    continue;
                }

                JobInfo info = new JobInfo();

                if (user ==null){
                    continue;
                }
                if (city == null){
                    continue;
                }

                info.setUserId(user.getId());
                info.setUserName(user.getName());

                info.setCountriesId(city.getCountriesId());
                info.setCountriesName(city.getCountriesName());
                info.setCityId(city.getId());
                info.setCityName(city.getName());

                info.setCompanyName(job.getCompanyName());
                info.setTitle(job.getTitle());
                info.setTel(job.getTel());

                info.setJobTypeName(job.getJobTypeName());
                info.setJobRequirementsName(job.getJobRequirementsName());
                info.setJobWelfareName(job.getJobWelfareName());
                info.setJobNatureName(job.getJobNatureName());

                info.setJobSalaryName(job.getJobSalaryName());
                String salaryOrder = Data.dao.getCodeDescByCodeAndType(job.getJobSalaryName(),"JOB_SALARY");
                if (StrKit.isBlank(salaryOrder)){
                    continue;
                }
                info.setJobSalaryOrder(Integer.parseInt(salaryOrder));

                info.setUpdateDate(new Date());
                info.setCreateDate(new Date());
                info.save();

            }
            renderJson(R.ok("导入成功"));
        }else {
            renderJson(R.ok("上传失败, 请检测文件类型是否正确, 请使用 xlsx 格式的Excel文件!"));
        }

    }

    /**
     * pc 后台用户管理 - 列表
     */
    public void userIndex(){
        int page = getParaToInt("_page", 1);
        UserInfo info = new UserInfo();
        info.setName(getPara("name"));


        Page<UserInfo> list = UserInfo.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
        keepPara();
        setAttr("page", list);
        render("userList.html");

    }

    /**
     * 更新黑名单
     */
    public void updateBlacklist(){
        int userId = getParaToInt("id");
        String blacklist = getPara("blacklist","0");
        UserInfo userInfo = new UserInfo();
        userInfo.setBlacklist(blacklist);
        userInfo.setId(userId);

        userInfo.update();
        setSessionSuccessMessage("操作成功");
        redirect("/job/pc/userIndex");
    }

	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage("导入/导出 失败");
		redirect("/job/pc");
	}

}
