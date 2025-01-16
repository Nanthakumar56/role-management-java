package com.springboot.manageroles.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exceptcriteria")
public class ExcludeFields {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
	private String ecid;
	private String tablename;
	private String fieldname;
	
	
	public ExcludeFields() {
		super();
	}

	public ExcludeFields(String ecid, String tablename, String fieldname) {
		super();
		this.ecid = ecid;
		this.tablename = tablename;
		this.fieldname = fieldname;
	}
	
	public String getEcid() {
		return ecid;
	}
	public void setEcid(String ecid) {
		this.ecid = ecid;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	@Override
	public String toString() {
		return "ExcludeFields [ecid=" + ecid + ", tablename=" + tablename + ", fieldname=" + fieldname + "]";
	}
	
	
}
