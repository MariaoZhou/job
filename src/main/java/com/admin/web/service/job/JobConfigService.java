package com.admin.web.service.job;

import com.admin.web.base.BaseBussinessService;
import com.admin.web.model.City;
import com.admin.web.model.Countries;
import com.admin.web.model.JobData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招聘 参数 服务
 */
public class JobConfigService extends BaseBussinessService {

    public static final JobConfigService me = new JobConfigService();

    /**
     * 获取位置
     */
    public List<Countries>  getLocation(){
        List<Countries> countriesList = Countries.dao.find("select * from " + Countries.table);
        for (Countries cc : countriesList){
            cc.put("cityList",cc.getCityList());
        }
        return countriesList;
    }

    /**
     * 获取 搜索 职位 参数
     */
    public Map<String, Object> searchJobConfig(String countries){
        // 返回值 map
        Map<String, Object> map = new HashMap<>();

        // 城市
        List<City> cityList = City.dao.find("select * from j_city where countriesId = ?" ,countries);
        // 工作种类 值 JOB_TYPE
        List<Map<String, String>> jobType = JobData.dao.getDateListByType("JOB_TYPE");
        // 工作性质 值 JOB_NATURE
        List<Map<String, String>> jobNature = JobData.dao.getDateListByType("JOB_NATURE");

        map.put("cityList", cityList);
        map.put("jobType", jobType);
        map.put("jobNature", jobNature);

        return map;
    }

    /**
     * 职位 参数
     * @param countries
     * @return
     */
    public Map<String ,Object> jobInfoConfig (String countries){
        // 返回值 map
        Map<String, Object> map = new HashMap<>();

        // 城市
        List<City> cityList = City.dao.find("select * from j_city where countriesId = ?" ,countries);
        // 工作种类 值 JOB_TYPE
        List<Map<String, String>> jobType = JobData.dao.getDateListByType("JOB_TYPE");
        // 工作性质 值 JOB_NATURE
        List<Map<String, String>> jobNature = JobData.dao.getDateListByType("JOB_NATURE");
        // 福利待遇 值 JOB_WELFARE
        List<Map<String, String>> jobWelfare = JobData.dao.getDateListByType("JOB_WELFARE");
        // 薪资区间 值 JOB_SALARY
        List<Map<String, String>> jobSalary = JobData.dao.getDateListByType("JOB_SALARY");
        // 居留要求 值 JOB_REQUIREMENTS
        List<Map<String, String>> jobRequirements = JobData.dao.getDateListByType("JOB_REQUIREMENTS");


        map.put("cityList", cityList);
        map.put("jobType", jobType);
        map.put("jobNature", jobNature);
        map.put("jobWelfare", jobWelfare);
        map.put("jobSalary", jobSalary);
        map.put("jobRequirements", jobRequirements);

        return map;

    }
}
