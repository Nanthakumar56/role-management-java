package com.springboot.manageroles.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Roles {
	
	@Id
	private String roleid;
	private String rolename;
	private String roledescription;
	private String status;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	public Roles() {
		super();
	}
	
	public Roles(String roleid, String rolename, String roledescription, String status, LocalDateTime created_at,
			LocalDateTime updated_at) {
		super();
		this.roleid = roleid;
		this.rolename = rolename;
		this.roledescription = roledescription;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getrolename() {
		return rolename;
	}
	public void setrolename(String rolename) {
		this.rolename = rolename;
	}
	public String getroledescription() {
		return roledescription;
	}
	public void setroledescription(String roledescription) {
		this.roledescription = roledescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	
}
