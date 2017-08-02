package com.admin.web.model;

import com.admin.web.model.base.BaseCountries;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Countries extends BaseCountries<Countries> {
	public static final Countries dao = new Countries().dao();
	public static final String table = "j_countries";

    public List<City> cityList() {
        System.out.println("xxxxxxxxxxxxxxxxxxxx");
        return City.dao.find("select * from j_city where countriesId = ?" , get("id"));
    }

}
