package com.admin.web.model;

import com.admin.web.model.base.BaseUserCompany;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class UserCompany extends BaseUserCompany<UserCompany> {
	public static final UserCompany dao = new UserCompany().dao();
	public static final String table = "user_company";
}
