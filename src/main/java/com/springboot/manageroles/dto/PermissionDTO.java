package com.springboot.manageroles.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PermissionDTO {

	private String id;
	private String roleid;
	private String functionality;
	private boolean isread;
	private boolean iscreate;
	private boolean isedit;
	private boolean isdelete;
	private LocalDateTime created_at;
    private List<AdvCriteriaDTO> criteria;

    public PermissionDTO() {}

	public PermissionDTO(String id, String roleid, String functionality, boolean isread, boolean iscreate,
			boolean isedit, boolean isdelete, LocalDateTime created_at, List<AdvCriteriaDTO> criteria) {
		super();
		this.id = id;
		this.roleid = roleid;
		this.functionality = functionality;
		this.isread = isread;
		this.iscreate = iscreate;
		this.isedit = isedit;
		this.isdelete = isdelete;
		this.created_at = created_at;
		this.criteria = criteria;
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

	public List<AdvCriteriaDTO> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<AdvCriteriaDTO> criteria) {
		this.criteria = criteria;
	}

	@Override
	public String toString() {
		return "PermissionDTO [id=" + id + ", roleid=" + roleid + ", functionality=" + functionality + ", isread="
				+ isread + ", iscreate=" + iscreate + ", isedit=" + isedit + ", isdelete=" + isdelete + ", created_at="
				+ created_at + ", criteria=" + criteria + "]";
	}

    
}
