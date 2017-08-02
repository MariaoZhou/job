package com.admin.web.model;

import com.rlax.framework.common.CacheKey;
import com.rlax.framework.common.Status;
import com.rlax.web.model.Data;
import com.rlax.web.model.base.BaseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JobData extends BaseData<Data> {
    public static final JobData dao = new JobData();
    public static final String table = "website_data";


    /**
     * 根据 type/status 获取数据字典列表
     * @param type 类型编码
     * @return
     */
    public List<Map<String, String>> getDateListByType(String type) {
        List<Map<String, String>> mapList = new ArrayList<>();

        List<String> params = new ArrayList<String>();
        params.add(type);

        String status = Status.COMMON_USE;
        params.add(status);

        List<Data> dataList = Data.dao.findByCache(CacheKey.CACHE_KEYVALUE, type + status, "select * from " + Data.table + " where type = ? and  status = ? order by orderNo asc" , params.toArray());

        for (Data dd : dataList){
            Map<String , String> map = new HashMap<>();
            map.put("code", dd.getCode());
            map.put("type", dd.getType());
            map.put("orderNo", dd.getOrderNo());

            mapList.add(map);
        }

        return mapList;
    }
}
