package com.admin.web.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUserCollection<M extends BaseUserCollection<M>> extends Model<M> implements IBean {

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

	public void setTitel(java.lang.String titel) {
		set("titel", titel);
	}

	public java.lang.String getTitel() {
		return get("titel");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setJobId(java.lang.Integer jobId) {
		set("jobId", jobId);
	}

	public java.lang.Integer getJobId() {
		return get("jobId");
	}

}