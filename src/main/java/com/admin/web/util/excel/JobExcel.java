package com.admin.web.util.excel;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * excel  job 实体
 *
 */
public class JobExcel {

    @Excel(name = "用户id")
    private String userId ;

    @Excel(name = "标题")
    private String title;

    @Excel(name = "联系电话")
    private String tel;

    @Excel(name = "工作地点(国家)")
    private String countriesName;

    @Excel(name = "工作地点(城市)")
    private String cityName;

    @Excel(name = "工作种类")
    private String jobTypeName;

    @Excel(name = "工作性质")
    private String jobNatureName;

    @Excel(name = "工作福利")
    private String jobWelfareName;

    @Excel(name = "薪资待遇")
    private String jobSalaryName;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCountriesName() {
        return countriesName;
    }

    public void setCountriesName(String countriesName) {
        this.countriesName = countriesName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getJobNatureName() {
        return jobNatureName;
    }

    public void setJobNatureName(String jobNatureName) {
        this.jobNatureName = jobNatureName;
    }

    public String getJobWelfareName() {
        return jobWelfareName;
    }

    public void setJobWelfareName(String jobWelfareName) {
        this.jobWelfareName = jobWelfareName;
    }

    public String getJobSalaryName() {
        return jobSalaryName;
    }

    public void setJobSalaryName(String jobSalaryName) {
        this.jobSalaryName = jobSalaryName;
    }
}
