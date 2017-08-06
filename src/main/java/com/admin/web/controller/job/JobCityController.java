package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.City;
import com.admin.web.model.Countries;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.rlax.framework.common.Consts;
import com.rlax.framework.exception.BusinessException;
import com.rlax.web.base.MessageBean;

/**
 * 城市
 */
@ControllerBind(controllerKey = "/job/city")
public class JobCityController extends BaseBussinessController {

	public void index() {
		int page = getParaToInt("_page", 1);
		City info = new City();

		Page<City> list = City.dao.findPage(info, page, Consts.PAGE_DEFAULT_SIZE);
		keepPara();
		setAttr("page", list);
		render("main.html");
	}
	
	public void add() {
		createToken();
		render("add.html");
	}

	public void do_add() {

        City info = getModel(City.class, "info");

        Countries countries = Countries.dao.findById(info.getCountriesId());
        info.setCountriesName(countries.getName());

		MessageBean message = new MessageBean();
		message.success();

		try {
		    if (info.getId()!=null){
		        info.update();
            }else {
		        info.save();
            }
		} catch (BusinessException e) {
			message.error(e.getMessage());
		}
		
		setSessionMessage(message);
		redirect("/job/city");
	}
	
	public void update() {
		createToken();	
		Integer id = getParaToInt("id");
        City info = City.dao.findById(id);

		setAttr("info", info).render("update.html");
	}	


	public void do_update() {

        City info = getModel(City.class, "info");

		MessageBean message = new MessageBean();
		message.success();

		info.update();

		setSessionMessage(message);
		redirect("/job/city");
	}
	
	public void delete() {
        City.dao.deleteById(getParaToLong("id"));
		MessageBean message = new MessageBean();
		message.success();
		setSessionMessage(message);
		redirect("/job/city");
	}
	

	
	@Override
	public void onExceptionError(Exception e) {
		setSessionErrorMessage(e.getMessage());
		redirect("/job/city");	}

}
