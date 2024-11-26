package com.springboot.manageroles.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class RolePayload {
    private String roleid;
    private String rolename;
    private String roledescription;
	private String status;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
    private Map<String, PermissionFlags> permissions;

    public RolePayload() {
		super();
	}

	public RolePayload(String roleid, String rolename, String roledescription, String status, LocalDateTime created_at,
			LocalDateTime updated_at, Map<String, PermissionFlags> permissions) {
		super();
		this.roleid = roleid;
		this.rolename = rolename;
		this.roledescription = roledescription;
		this.status = status;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.permissions = permissions;
	}
	
	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledescription() {
		return roledescription;
	}

	public void setRoledescription(String roledescription) {
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

	public Map<String, PermissionFlags> getPermissions() {
		return permissions;
	}

	public void setPermissions(Map<String, PermissionFlags> permissions) {
		this.permissions = permissions;
	}





	// Nested class for CRUD flags
    public static class PermissionFlags {
        private boolean read;
        private boolean edit;
        private boolean create;
        private boolean delete;

        public PermissionFlags() {
            super();
        }

        public PermissionFlags(boolean read, boolean edit, boolean create, boolean delete) {
            this.read = read;
            this.edit = edit;
            this.create = create;
            this.delete = delete;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public boolean isEdit() {
            return edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public boolean isCreate() {
            return create;
        }

        public void setCreate(boolean create) {
            this.create = create;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }
    }
}
