package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.City;
import com.admin.web.model.Countries;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;

import java.util.List;

/**
 *  招聘 首页 controller
 */
@ControllerBind(controllerKey = "/job/index")
public class JobIndexController extends BaseBussinessController {

    /**
     * 获取位置
     */
    public void getLocation(){
        List<Countries> countrieList = Countries.dao.find("select * from " + Countries.table);

        for (Countries cc : countrieList){
            cc.getCityList();
            System.out.println("cc = " + cc.getCityList());
        }


//        for (Countries cc : countries){
//            City city = cc.getCity();
//            System.out.println("city = " + city);
//            System.out.println(cc.get("city"));
//        }

        renderJson(R.ok().put(countrieList));

    }




	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
