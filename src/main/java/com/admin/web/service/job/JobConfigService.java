package com.admin.web.service.job;

import com.admin.web.base.BaseBussinessService;
import com.admin.web.model.*;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.rlax.web.model.Data;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 招聘 参数 服务
 */
public class JobConfigService extends BaseBussinessService {

    public static final JobConfigService me = new JobConfigService();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");   //2017-08-09 12:04:07.0
    /**
     * 职位条件查询
     * @param job
     * @param pageNumber
     * @return
     */
    @Before(Tx.class)
    public Page<JobInfo> searchJobInfo(JobInfo job, String type, Integer userId, Integer pageNumber,Integer pageSize){
        String userSql = "";
        List<String> params = new ArrayList<>();

        if (userId !=null){
            userSql = "and c.userId = ? ";
            params.add(userId.toString());
        }
        params.add(job.getCountriesId().toString());

        StringBuilder from = new StringBuilder("from " + JobInfo.table +
                                                " o LEFT JOIN user_collection c on o.id = c.jobId and c.type = '1' " +userSql +
                                                " where o.countriesId = ?");
        String order = " order by o.updateDate DESC";



        if (StrKit.notBlank(job.getCityName())) {
            String[] param = job.getCityName().split(",");
            from.append(" and ( instr(o.cityName, ?) > 0 ");
            params.add(param[0]);
            for (int i=1 ; i<param.length; i++){
                from.append("OR instr(o.cityName, ?) > 0 ");
                params.add(param[i]);
            }
            from.append(" )");
        }
        if (StrKit.notBlank(job.getTitle())) {
            from.append(" and instr(o.title, ?) > 0 ");
            params.add(job.getTitle());
        }
        if (StrKit.notBlank(job.getJobTypeName())) {
            String[] typeArr = job.getJobTypeName().split(",");
            from.append(" and ( instr(o.jobTypeName, ?) > 0 ");
            params.add(typeArr[0]);
            for (int i=1 ; i<typeArr.length; i++){
                from.append("OR instr(o.jobTypeName, ?) > 0 ");
                params.add(typeArr[i]);
            }
            from.append(" )");
        }
        if (StrKit.notBlank(job.getJobNatureName())) {
            String[] param = job.getJobNatureName().split(",");
            from.append(" and ( instr(o.jobNatureName, ?) > 0 ");
            params.add(param[0]);
            for (int i=1 ; i<param.length; i++){
                from.append("OR instr(o.jobNatureName, ?) > 0 ");
                params.add(param[i]);
            }
            from.append(" )");
        }
        // 类型 最高工资1 企业查询2
        if ("1".equals(type)){
            order = " order by o.jobSalaryOrder DESC";
        }else if ("2".equals(type)){
            from.append(" and o.companyName != '' ");
        }

        from.append(order);

        Page<JobInfo> jobInfoList = JobInfo.dao.paginate(pageNumber, pageSize,
                                 "select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId",
                                    from.toString(), params.toArray());

        return jobInfoList;
    }

    /**
     * 找人办事 条件查询
     * @param someone
     * @param pageNumber
     * @return
     */
    @Before(Tx.class)
    public Page<Someone> searchSomeone(Someone someone, Integer userId ,Integer pageNumber , Integer pageSize){

        String userSql = "";
        List<String> params = new ArrayList<>();

        if (userId !=null){
            userSql = " and c.userId = ? ";
            params.add(userId.toString());
        }
        params.add(someone.getCountriesId().toString());

        StringBuilder from = new StringBuilder("from " + Someone.table +
                                " o LEFT JOIN user_collection c on o.id = c.jobId and c.type = '1' " + userSql +
                                " where o.countriesId = ?");


        if (StrKit.notBlank(someone.getSomeoneTypeName())){
            String[] param = someone.getSomeoneTypeName().split(",");
            from.append(" and ( instr(someoneTypeName, ?) > 0 ");
            params.add(param[0]);
            for (int i=1 ; i<param.length; i++){
                from.append("OR instr(someoneTypeName, ?) > 0 ");
                params.add(param[i]);
            }
            from.append(" )");

        }
        if (StrKit.notBlank(someone.getTitle())){
            from.append( " and instr(title, ?) > 0 ");
            params.add(someone.getTitle());
        }


        from.append(" order by updateDate DESC");
        Page<Someone> someonePage = Someone.dao.paginate(pageNumber, pageSize, "select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId ", from.toString(), params.toArray());
        return someonePage;
    }

    /**
     * 获取默认列表 职位and找人办事
     */
    @Before(Tx.class)
    public List<Map> searchJobIndex(String countries, Integer userId){

        Integer pageNumber = 1;
        Integer pageSize = 9999;
        List<String> params = new ArrayList<>();
        String sql = " ";
        if (userId !=null){
            sql = "and c.userId = " + userId;
            params.add(userId.toString());
        }
        params.add(countries);

        // 职位 集合
        String jFrom = "from j_job_info o LEFT JOIN user_collection c on o.id = c.jobId and c.type = '1' " + sql +" where o.countriesId = ?";
        Page<JobInfo> jobInfoList = JobInfo.dao.paginate(pageNumber, pageSize,"select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId ", jFrom, params.toArray());
        // 找人办事 集合
        String sFrom = "from j_someone o LEFT JOIN user_collection c on o.id = c.jobId and c.type = '2' "+ sql +" where o.countriesId = ? ";
        Page<Someone> someoneList = Someone.dao.paginate(pageNumber, pageSize, "select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId ", sFrom, params.toArray());

        List<Map> mapList = new ArrayList<>();

        for (JobInfo jobInfo : jobInfoList.getList()){
            mapList.add(toMap(jobInfo));
        }
        for (Someone someone : someoneList.getList()){
            mapList.add(toMap(someone));
        }

        Collections.sort(mapList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                if (o1.get("updateDate") == null && o2.get("updateDate") == null)
                    return 0;
                if (o1.get("updateDate") == null)
                    return -1;
                if (o2.get("updateDate") == null)
                    return 1;
                try {
                    Date o22 = formatter.parse(o2.get("updateDate").toString());
                    Long o222 = o22.getTime();

                    Date o11 = formatter.parse(o1.get("updateDate").toString());
                    Long o111 = o11.getTime();

                    return o222.compareTo(o111);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return mapList;
    }


    /**
     * 组合查询 我的收藏
     */
    @Before(Tx.class)
    public List<Map> searchCollection(Integer userId){

        Integer pageNumber = 1;
        Integer pageSize = 9999;
        // 职位 集合
        String jFrom = "from j_job_info o LEFT JOIN user_collection c on o.id = c.jobId and c.type = '1' where c.userId = ?";
        Page<JobInfo> jobInfoList = JobInfo.dao.paginate(pageNumber, pageSize,"select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId", jFrom, userId);
        // 找人办事 集合
        String sFrom = "from j_someone o LEFT JOIN user_collection c on o.id = c.jobId and c.type = '2' where c.userId = ?";
        Page<Someone> someoneList = Someone.dao.paginate(pageNumber, pageSize, "select o.*, c.jobId as cJobId, c.id as cId, c.userId as cUserId", sFrom, userId);

        List<Map> mapList = new ArrayList<>();

        for (JobInfo jobInfo : jobInfoList.getList()){
            mapList.add(toMap(jobInfo));
        }
        for (Someone someone : someoneList.getList()){
            mapList.add(toMap(someone));
        }

        Collections.sort(mapList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                if (o1.get("updateDate") == null && o2.get("updateDate") == null)
                    return 0;
                if (o1.get("updateDate") == null)
                    return -1;
                if (o2.get("updateDate") == null)
                    return 1;
                try {
                    Date o22 = formatter.parse(o2.get("updateDate").toString());
                    Long o222 = o22.getTime();

                    Date o11 = formatter.parse(o1.get("updateDate").toString());
                    Long o111 = o11.getTime();

                    return o222.compareTo(o111);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        return mapList;
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

            someone.setStatus("0");
            if (someone.getId()!=null){
                someone.setUpdateDate(new Date());
                return someone.update();
            }else {
                someone.setUpdateDate(new Date());
                someone.setCreateDate(new Date());
                return someone.save();
            }
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

            job.setStatus("0");

            if (job.getId()!=null){
                job.setUpdateDate(new Date());
                return job.update();
            }else {
                job.setUpdateDate(new Date());
                job.setCreateDate(new Date());
                return job.save();
            }
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


    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    private Map toMap(Object bean) {
        Class<? extends Object> clazz = bean.getClass();
        Map<Object, Object> returnMap = new HashMap<Object, Object>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = null;
                    result = readMethod.invoke(bean, new Object[0]);
                    if (null != propertyName) {
                        propertyName = propertyName.toString();
                    }
                    if (null != result) {
                        result = result.toString();
                    }
                    returnMap.put(propertyName, result);
                }
            }
        } catch (IntrospectionException e) {
            System.out.println("分析类属性失败");
        } catch (IllegalAccessException e) {
            System.out.println("实例化 JavaBean 失败");
        } catch (IllegalArgumentException e) {
            System.out.println("映射错误");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("调用属性的 setter 方法失败");
        }
        return returnMap;
    }
}
