package com.admin.web.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSomeone<M extends BaseSomeone<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setCountriesId(java.lang.Integer countriesId) {
		set("countriesId", countriesId);
	}

	public java.lang.Integer getCountriesId() {
		return get("countriesId");
	}

	public void setCountriesName(java.lang.String countriesName) {
		set("countriesName", countriesName);
	}

	public java.lang.String getCountriesName() {
		return get("countriesName");
	}

	public void setCompanyName(java.lang.String companyName) {
		set("companyName", companyName);
	}

	public java.lang.String getCompanyName() {
		return get("companyName");
	}

	public void setCompanyPublicity(java.lang.String companyPublicity) {
		set("companyPublicity", companyPublicity);
	}

	public java.lang.String getCompanyPublicity() {
		return get("companyPublicity");
	}

	public void setCompanyQRCode(java.lang.String companyQRCode) {
		set("companyQRCode", companyQRCode);
	}

	public java.lang.String getCompanyQRCode() {
		return get("companyQRCode");
	}

	public void setCompanyInfo(java.lang.String companyInfo) {
		set("companyInfo", companyInfo);
	}

	public java.lang.String getCompanyInfo() {
		return get("companyInfo");
	}

	public void setCompanyLogo(java.lang.String companyLogo) {
		set("companyLogo", companyLogo);
	}

	public java.lang.String getCompanyLogo() {
		return get("companyLogo");
	}

	public void setUserId(java.lang.Integer userId) {
		set("userId", userId);
	}

	public java.lang.Integer getUserId() {
		return get("userId");
	}

	public void setUserName(java.lang.String userName) {
		set("userName", userName);
	}

	public java.lang.String getUserName() {
		return get("userName");
	}

	public void setCityId(java.lang.Integer cityId) {
		set("cityId", cityId);
	}

	public java.lang.Integer getCityId() {
		return get("cityId");
	}

	public void setCityName(java.lang.String cityName) {
		set("cityName", cityName);
	}

	public java.lang.String getCityName() {
		return get("cityName");
	}

	public void setSomeoneTypeName(java.lang.String someoneTypeName) {
		set("someoneTypeName", someoneTypeName);
	}

	public java.lang.String getSomeoneTypeName() {
		return get("someoneTypeName");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setTel(java.lang.String tel) {
		set("tel", tel);
	}

	public java.lang.String getTel() {
		return get("tel");
	}

	public void setDetails(java.lang.String details) {
		set("details", details);
	}

	public java.lang.String getDetails() {
		return get("details");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("createDate", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("createDate");
	}

	public void setUpdateDate(java.util.Date updateDate) {
		set("updateDate", updateDate);
	}

	public java.util.Date getUpdateDate() {
		return get("updateDate");
	}

}
