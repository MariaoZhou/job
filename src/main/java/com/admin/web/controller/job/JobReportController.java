package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobInfo;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.rlax.web.model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职位统计
 */
@ControllerBind(controllerKey = "/job/report")
public class JobReportController extends BaseBussinessController {

	public void index() {

        String data = Data.dao.getCodeByCodeDescAndType("REPORT_KEY", "统计关键字");
        String[] keys = data.split(",");
        List<JobInfo> jobInfo =JobInfo.dao.find("select * from "+ JobInfo.table);


        String sql = "SELECT count(j.title) as 'report' FROM j_job_info j where instr(j.title, ?);";
        List<Map<String, String>> reportList = new ArrayList<>();
        for (String key : keys){
            Map<String, String> report = new HashMap<>();
            Record record = Db.findFirst(sql , key);
            report.put("key", key);
            report.put("num", record.get("report").toString());
            reportList.add(report);

        }

		keepPara();
		setAttr("list", reportList).setAttr("count", jobInfo.size());
		render("main.html");
	}

	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/job/report");
	}

}
