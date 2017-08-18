package com.admin.web.test;

import com.admin.web.model.City;
import com.admin.web.model.Countries;
import com.admin.web.model.JobInfo;
import com.admin.web.model.UserInfo;
import com.admin.web.util.DateUtils;
import com.admin.web.util.excel.JobExcel;
import com.rlax.web.model.Data;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by xvtie on 2017/6/25.
 */
public class JobTest {

    @Test
    public void excel(){

        ImportParams params = new ImportParams();
        List<JobExcel> list = null;
        try {
            list = ExcelImportUtil.importExcel(new FileInputStream("D:/111.xlsx"), JobExcel.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (JobExcel job : list){
            System.out.println(job.toString());


                JobInfo info = new JobInfo();

                UserInfo user = UserInfo.dao.findById(job.getUserId());
                info.setUserId(user.getId());
                info.setUserName(user.getName());

                City city = City.dao.findFirst("select * from j_city where name = ?" , job.getCityName());
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
                info.setJobSalaryOrder(Integer.parseInt(salaryOrder));


                info.save();

            //System.out.println(sql);
        }

    }

    @Test
    public void  jobIndex(){
        List<Countries> countries = Countries.dao.find("select * from ? ", Countries.table);

        System.out.println(countries);


    }
}
