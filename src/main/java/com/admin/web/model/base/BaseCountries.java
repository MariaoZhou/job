package com.admin.web.model.base;

import com.admin.web.model.City;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCountries<M extends BaseCountries<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setCurrency(java.lang.String currency) {
		set("currency", currency);
	}

	public java.lang.String getCurrency() {
		return get("currency");
	}

    public List<City> getCityList() {
        return City.dao.find("select * from j_city where countriesId = ?" , get("id"));
    }
}