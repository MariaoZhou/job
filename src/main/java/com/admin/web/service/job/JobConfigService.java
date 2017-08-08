package com.admin.web.service.job;

import com.admin.web.base.BaseBussinessService;
import com.admin.web.model.*;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.rlax.framework.common.Consts;
import com.rlax.web.model.Data;

import java.util.*;

/**
 * 招聘 参数 服务
 */
public class JobConfigService extends BaseBussinessService {

    public static final JobConfigService me = new JobConfigService();

    /**
     * 职位条件查询
     * @param job
     * @param pageNumber
     * @return
     */
    @Before(Tx.class)
    public Page<JobInfo> searchJobInfo(JobInfo job, String type, Integer pageNumber){
        String from = "from " + JobInfo.table + " where countriesId = ?";
        String order = " order by createDate asc";
        List<String> params = new ArrayList<>();
        params.add(job.getCountriesId().toString());

        if (job.getCityId()!=0) {
            from += " and instr(cityId, ?) > 0 ";
            params.add(job.getCityId().toString());
        }
        if (StrKit.notBlank(job.getTitle())) {
            from += " and instr(title, ?) > 0 ";
            params.add(job.getTitle());
        }
        if (StrKit.notBlank(job.getJobTypeName())) {
            from += " and instr(jobTypeName, ?) > 0 ";
            params.add(job.getJobTypeName());
        }
        if (StrKit.notBlank(job.getJobNatureName())) {
            from += " and instr(jobNatureName, ?) > 0 ";
            params.add(job.getJobNatureName());
        }
        // 类型 最高工资1 企业查询2
        if ("1".equals(type)){
            order = " order by jobSalaryOrder asc";
        }else if ("1".equals(type)){
            from += " and companyName != '' ";
        }

        from += order;

        Page<JobInfo> jobInfoList = JobInfo.dao.paginate(pageNumber, Consts.PAGE_DEFAULT_SIZE, "select * ", from, params.toArray());

        return jobInfoList;
    }

    /**
     * 找人办事 条件查询
     * @param someone
     * @param pageNumber
     * @return
     */
    @Before(Tx.class)
    public Page<Someone> searchSomeone(Someone someone ,Integer pageNumber){
        String from = "from " + Someone.table + " where countriesId = ?";
        List<String> params = new ArrayList<>();
        params.add(someone.getCountriesId().toString());

        if (StrKit.notBlank(someone.getSomeoneTypeName())){
            from += " and instr(someoneTypeName, ?) > 0 ";
        }
        if (StrKit.notBlank(someone.getTitle())){
            from += " and instr(title, ?) > 0 ";
        }


        from += " order by createDate asc";
        Page<Someone> someonePage = Someone.dao.paginate(pageNumber, Consts.PAGE_DEFAULT_SIZE, "select * ", from, params.toArray());
        return someonePage;
    }


    /**
     * 找人办事 保存
     * @param someone
     * @param cityId
     * @param userId
     * @return
     */
    @Before(Tx.class)
    public boolean saveSomeone(Someone someone, String cityId, String userId){
        try {
            City city = City.dao.findById(cityId);
            someone.setCityId(city.getId());
            someone.setCityName(city.getName());
            someone.setCountriesId(city.getCountriesId());
            someone.setCountriesName(city.getCountriesName());

            UserInfo userInfo = UserInfo.dao.findById(userId);
            someone.setUserName(userInfo.getName());
            someone.setUserId(userInfo.getId());

            someone.setCreateDate(new Date());
            someone.setUpdateDate(new Date());
            return someone.save();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 职位 保存
     * @param job
     * @param cityId
     * @param userId
     * @return
     */
    @Before(Tx.class)
    public boolean saveJobInfo(JobInfo job, String cityId, String userId){
        try {
            City city = City.dao.findById(cityId);
            job.setCityId(city.getId());
            job.setCityName(city.getName());
            job.setCountriesId(city.getCountriesId());
            job.setCountriesName(city.getCountriesName());

            UserInfo userInfo = UserInfo.dao.findById(userId);
            job.setUserId(userInfo.getId());
            job.setUserName(userInfo.getName());

            // 薪资排序
            String salaryOrder = Data.dao.getCodeDescByCodeAndType(job.getJobSalaryName(),"JOB_SALARY");
            job.setJobSalaryOrder(Integer.parseInt(salaryOrder));

            job.setCreateDate(new Date());
            job.setUpdateDate(new Date());

            return job.save();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取位置
     */
    @Before(Tx.class)
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
    @Before(Tx.class)
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
    @Before(Tx.class)
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
