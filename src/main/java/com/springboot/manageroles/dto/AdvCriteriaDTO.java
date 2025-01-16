package com.springboot.manageroles.dto;

public class AdvCriteriaDTO {

	private String advcid;
	private String permissionid;
	private String roleid;
	private String criterianame;
	private String operation;
	private String criteriavalue;
	private String function;

    public AdvCriteriaDTO() {}

	public AdvCriteriaDTO(String advcid, String permissionid, String roleid, String criterianame, String operation,
			String criteriavalue, String function) {
		super();
		this.advcid = advcid;
		this.permissionid = permissionid;
		this.roleid = roleid;
		this.criterianame = criterianame;
		this.operation = operation;
		this.criteriavalue = criteriavalue;
		this.function = function;
	}

	public String getAdvcid() {
		return advcid;
	}

	public void setAdvcid(String advcid) {
		this.advcid = advcid;
	}

	public String getPermissionid() {
		return permissionid;
	}

	public void setPermissionid(String permissionid) {
		this.permissionid = permissionid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getCriterianame() {
		return criterianame;
	}

	public void setCriterianame(String criterianame) {
		this.criterianame = criterianame;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCriteriavalue() {
		return criteriavalue;
	}

	public void setCriteriavalue(String criteriavalue) {
		this.criteriavalue = criteriavalue;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return "AdvCriteriaDTO [advcid=" + advcid + ", permissionid=" + permissionid + ", roleid=" + roleid
				+ ", criterianame=" + criterianame + ", operation=" + operation + ", criteriavalue=" + criteriavalue
				+ ", function=" + function + "]";
	}

	
    
}
