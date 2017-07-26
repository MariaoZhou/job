package com.admin.web.model;

import com.admin.web.model.base.BaseJobMessageWindow;

/**
 * 聊天会话
 */
@SuppressWarnings("serial")
public class JobMessageWindow extends BaseJobMessageWindow<JobMessageWindow> {
	public static final JobMessageWindow dao = new JobMessageWindow().dao();
	public static final String table = "job_message_window";
}
