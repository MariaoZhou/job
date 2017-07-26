package com.admin.web.model;

import com.admin.web.model.base.BaseJobMessageDetails;

/**
 * 聊天会话 > 详情
 */
@SuppressWarnings("serial")
public class JobMessageDetails extends BaseJobMessageDetails<JobMessageDetails> {
	public static final JobMessageDetails dao = new JobMessageDetails().dao();
	public static final String table = "job_message_details";
}
