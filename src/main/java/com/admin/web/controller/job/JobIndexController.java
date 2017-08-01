package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
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
        List<Countries> countries = Countries.dao.find("select * from " + Countries.table);

        for (Countries cc : countries){
            System.out.println(cc.get("CityList"));
        }

        renderJson(R.ok().put(countries));

    }




	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
