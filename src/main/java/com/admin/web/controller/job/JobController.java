package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobUnitPosition;
import com.admin.web.util.R;
import com.admin.web.util.excel.JobExcel;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.rlax.framework.common.Consts;
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
 * 招聘管理
 */
@ControllerBind(controllerKey = "/job/pc")
public class JobController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		JobUnitPosition info = new JobUnitPosition();
        info.setJob(getPara("job"));
        info.setCountry(getPara("country"));

		Page<JobUnitPosition> list = JobUnitPosition.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}

	public void delete(){
        String id = getPara("id");
        if (id.contains(",")){
            String[] ids = id.split(",");
            for (String ii :ids){
                JobUnitPosition info = JobUnitPosition.dao.findById(ii);
                info.delete();
            }
        }else {
            JobUnitPosition info = JobUnitPosition.dao.findById(id);
            info.delete();
        }

        setSessionSuccessMessage("操作成功");
        redirect("/job/pc");
    }

	/**
	 * excel 导出导入 跳转
	 */
	public void excel(){
        render("excel.html");
	}

    /**
     * excel 下载
     */
	public void excelDownload(){

	    List<JobUnitPosition> lists = JobUnitPosition.dao.find("select * from job_unit_position") ;

	    List<JobExcel> jobExcels = new ArrayList<>();

	    for (JobUnitPosition po : lists){
            JobExcel job = new JobExcel(
                    po.getJob(),po.getSalary(),po.getUnit(),po.getTel(),po.getCountry(),po.getCity(),po.getStreet(),po.getQualification(),po.getJobDetail(),po.getSkill(),po.getStrength()
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
     * excel 导入
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

                JobUnitPosition position = new JobUnitPosition();
                position.setJob(job.getName());
                position.setSalary(job.getSalary());
                position.setUnit(job.getUnit());
                position.setCountry(job.getCountry());
                position.setCity(job.getCity());
                position.setSkill(job.getSkill());
                position.setStreet(job.getStreet());
                position.setJobDetail(job.getJobDetail());
                position.setTel(job.getTel());
                position.setQualification(job.getQualification());
                position.setStrength(job.getStrength());
                position.setCreateDate(new Date());

                position.save();
            }
            renderJson(R.ok("导入成功"));
        }else {
            renderJson(R.ok("上传失败, 请检测文件类型是否正确, 请使用 xlsx 格式的Excel文件!"));
        }

    }

	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/job/pc");
	}

}
