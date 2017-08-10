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

    @Excel(name = "公司名称")
    private String companyName;

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

    @Excel(name = "居住要求")
    private String jobRequirementsName;

    @Excel(name = "职位详情")
    private String details;

    public JobExcel(String userId, String title, String companyName, String tel, String countriesName, String cityName, String jobTypeName, String jobNatureName, String jobWelfareName, String jobSalaryName, String jobRequirementsName, String details) {
        this.userId = userId;
        this.title = title;
        this.companyName = companyName;
        this.tel = tel;
        this.countriesName = countriesName;
        this.cityName = cityName;
        this.jobTypeName = jobTypeName;
        this.jobNatureName = jobNatureName;
        this.jobWelfareName = jobWelfareName;
        this.jobSalaryName = jobSalaryName;
        this.jobRequirementsName = jobRequirementsName;
        this.details = details;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRequirementsName() {
        return jobRequirementsName;
    }

    public void setJobRequirementsName(String jobRequirementsName) {
        this.jobRequirementsName = jobRequirementsName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

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
