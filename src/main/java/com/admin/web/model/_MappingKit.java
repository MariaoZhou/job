package com.admin.web.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("j_city", "id", City.class);
		arp.addMapping("j_countries", "id", Countries.class);
		arp.addMapping("j_company_info", "id", CompanyInfo.class);
		arp.addMapping("j_job_info", "id", JobInfo.class);
		arp.addMapping("j_someone", "id", Someone.class);
		arp.addMapping("user_collection", "id", UserCollection.class);
		arp.addMapping("user_info", "id", UserInfo.class);
		arp.addMapping("wx_msg", "id", WxMsg.class);
	}
}

