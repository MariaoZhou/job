package com.admin.web.util.excel;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * excel  job 实体
 *
 */
public class JobExcel {

    @Excel(name = "职位名称")
    private String name ;

    @Excel(name = "薪资")
    private String salary;

    @Excel(name = "公司名称")
    private String unit;

    @Excel(name = "联系电话")
    private String tel;

    @Excel(name = "工作地点(国家)")
    private String country;

    @Excel(name = "工作地点(城市)")
    private String city;

    @Excel(name = "工作地点(街道)")
    private String street;

    @Excel(name = "学历")
    private String qualification;

    @Excel(name = "职位详情")
    private String jobDetail;

    @Excel(name = "技能要求(以 ,分割)")
    private String skill;

    @Excel(name = "职位吸引力")
    private String strength;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"salary\":\"")
                .append(salary).append('\"');
        sb.append(",\"unit\":\"")
                .append(unit).append('\"');
        sb.append(",\"tel\":\"")
                .append(tel).append('\"');
        sb.append(",\"country\":\"")
                .append(country).append('\"');
        sb.append(",\"city\":\"")
                .append(city).append('\"');
        sb.append(",\"street\":\"")
                .append(street).append('\"');
        sb.append(",\"qualification\":\"")
                .append(qualification).append('\"');
        sb.append(",\"jobDetail\":\"")
                .append(jobDetail).append('\"');
        sb.append(",\"skill\":\"")
                .append(skill).append('\"');
        sb.append(",\"strength\":\"")
                .append(strength).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public JobExcel() {
    }

    public JobExcel(String name, String salary, String unit, String tel, String country, String city, String street, String qualification, String jobDetail, String skill, String strength) {

        this.name = name;
        this.salary = salary;
        this.unit = unit;
        this.tel = tel;
        this.country = country;
        this.city = city;
        this.street = street;
        this.qualification = qualification;
        this.jobDetail = jobDetail;
        this.skill = skill;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(String jobDetail) {
        this.jobDetail = jobDetail;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
