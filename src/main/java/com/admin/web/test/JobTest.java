package com.admin.web.test;

import com.admin.web.model.JobMember;
import com.admin.web.util.DateUtils;
import com.admin.web.util.excel.JobExcel;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by xvtie on 2017/6/25.
 */
public class JobTest {

    @Test
    public void excel(){

        ImportParams params = new ImportParams();
        List<JobExcel> list = ExcelImportUtil.importExcel(new File("D:/333.xlsx"), JobExcel.class, params);
        JobMember member = new JobMember();
        member.setId(null);
        member.setName(null);
        member.setImage(null);

        for (JobExcel job : list){
            //System.out.println(job.toString());

            String sql = String.format(
                    "INSERT INTO `job`.`job_unit_position` " +
                            "(`job`, `salary`, `unit`, `country`, `city`, `skill`, `street`, `jobDetail`, `tel`, `qualification`, `strength`, `createDate`) VALUES " +
                            "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",

                             job.getName(), job.getSalary(), job.getUnit(), job.getCountry(), job.getCity(),
                            job.getSkill(), job.getStreet(), job.getJobDetail(), job.getTel(), job.getQualification(), job.getStrength() , DateUtils.getDateTime());


            System.out.println(sql);
        }

    }
}