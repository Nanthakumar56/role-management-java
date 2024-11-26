package com.springboot.manageroles.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="permissions")
public class Permissions 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
	private String id;
	private String roleid;
	private String functionality;
	private boolean isread;
	private boolean iscreate;
	private boolean isedit;
	private boolean isdelete;
	private LocalDateTime created_at;
	public Permissions() {
		super();
	}
	public Permissions(String id, String roleid, String functionality, boolean isread, boolean iscreate, boolean isedit,
			boolean isdelete, LocalDateTime created_at) {
		super();
		this.id = id;
		this.roleid = roleid;
		this.functionality = functionality;
		this.isread = isread;
		this.iscreate = iscreate;
		this.isedit = isedit;
		this.isdelete = isdelete;
		this.created_at = created_at;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getFunctionality() {
		return functionality;
	}
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}
	public boolean isIsread() {
		return isread;
	}
	public void setIsread(boolean isread) {
		this.isread = isread;
	}
	public boolean isIscreate() {
		return iscreate;
	}
	public void setIscreate(boolean iscreate) {
		this.iscreate = iscreate;
	}
	public boolean isIsedit() {
		return isedit;
	}
	public void setIsedit(boolean isedit) {
		this.isedit = isedit;
	}
	public boolean isIsdelete() {
		return isdelete;
	}
	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}	
	
}
