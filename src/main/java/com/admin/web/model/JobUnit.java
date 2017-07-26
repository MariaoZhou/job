package com.admin.web.model;

import com.admin.web.model.base.BaseJobUnit;

/**
 * 公司信息
 */
@SuppressWarnings("serial")
public class JobUnit extends BaseJobUnit<JobUnit> {
	public static final JobUnit dao = new JobUnit().dao();
	public static final String table = "job_unit";
}
