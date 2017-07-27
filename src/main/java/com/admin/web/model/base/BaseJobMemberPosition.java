package com.admin.web.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseJobMemberPosition<M extends BaseJobMemberPosition<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setMemberId(java.lang.Integer memberId) {
		set("memberId", memberId);
	}

	public java.lang.Integer getMemberId() {
		return get("memberId");
	}

	public void setJobId(java.lang.Integer jobId) {
		set("jobId", jobId);
	}

	public java.lang.Integer getJobId() {
		return get("jobId");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

}