package com.admin.web.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUserCompany<M extends BaseUserCompany<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
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

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setPublicity(java.lang.String publicity) {
		set("publicity", publicity);
	}

	public java.lang.String getPublicity() {
		return get("publicity");
	}

	public void setQRCode(java.lang.String QRCode) {
		set("QRCode", QRCode);
	}

	public java.lang.String getQRCode() {
		return get("QRCode");
	}

	public void setInfo(java.lang.String info) {
		set("info", info);
	}

	public java.lang.String getInfo() {
		return get("info");
	}

	public void setLogo(java.lang.String logo) {
		set("logo", logo);
	}

	public java.lang.String getLogo() {
		return get("logo");
	}

}
