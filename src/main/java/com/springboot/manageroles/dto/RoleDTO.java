package com.springboot.manageroles.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class RoleDTO {

    private String roleId;
    private String roleName;
    private String roleDescription;
    private String status;
    private LocalDateTime created_at;
    private List<PermissionDTO> permissions;
    private int usersAssigned;

    public RoleDTO() {}

    public RoleDTO(String roleId, String roleName, String roleDescription, String status,
    		LocalDateTime created_at, int usersAssigned, List<PermissionDTO> permissions) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.status = status;
        this.created_at = created_at;
        this.permissions = permissions;
        this.usersAssigned = usersAssigned;
    }

    public LocalDateTime getCreated_at() {
		return created_at;
	}

	public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
    

	public int getUsersAssigned() {
		return usersAssigned;
	}

	public void setUsersAssigned(int usersAssigned) {
		this.usersAssigned = usersAssigned;
	}

	@Override
	public String toString() {
		return "RoleDTO [roleId=" + roleId + ", roleName=" + roleName + ", roleDescription=" + roleDescription
				+ ", status=" + status + ", created_at=" + created_at + ", permissions=" + permissions + "]";
	}

   
}
