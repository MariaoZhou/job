package com.admin.web.model;

import com.admin.web.model.base.BaseJobUnitPosition;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司信息 > 招聘职位信息
 */
@SuppressWarnings("serial")
public class JobUnitPosition extends BaseJobUnitPosition<JobUnitPosition> {
	public static final JobUnitPosition dao = new JobUnitPosition().dao();
	public static final String table = "job_unit_position";

    public Page<JobUnitPosition> findPage(JobUnitPosition info, int pageNumber, int pageSize) {
        String sql = " from " + table + " where 1 = 1 ";
        List<Object> pm = new ArrayList<Object>();
        if (StrKit.notBlank(info.getCountry())) {
            sql += " and instr(country, ?) > 0 ";
            pm.add(info.getCountry());
        }
        if (StrKit.notBlank(info.getJob())) {
            sql += " and instr(job, ?) > 0 ";
            pm.add(info.getJob());
        }
        sql += " order by createDate";
        Page<JobUnitPosition> page = dao.paginate(pageNumber, pageSize, "select *", sql,pm.toArray());
        return page;
    }
}
