package com.springboot.manageroles.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.manageroles.dto.RolePayload;
import com.springboot.manageroles.service.RoleService;

@Controller
@RequestMapping("/roles")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/newRole")
    public ResponseEntity<?> createRole(@RequestBody RolePayload role) {
        String rolePayload = roleService.createRole(role);

        if (rolePayload != null) {
            return ResponseEntity.status(HttpStatus.OK).body(rolePayload);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rolePayload);
        }
    }

	@GetMapping("/getallroles")
	public ResponseEntity<List<RolePayload>> getAllRoles() {
        List<RolePayload> rolePayloadList = roleService.getAllRolePayloads();

        if (!rolePayloadList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(rolePayloadList);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/getRole")
    public ResponseEntity<RolePayload> getRole(@RequestParam String roleId) {
        Optional<RolePayload> rolePayload = roleService.getRolePayloadById(roleId);

        return rolePayload.map(payload ->
                ResponseEntity.status(HttpStatus.OK).body(payload))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
    
    @PutMapping("/update")
    public ResponseEntity<RolePayload> updateRoleWithPermissions(
        @RequestBody RolePayload rolePayload
    ) {
        Optional<RolePayload> updatedRolePayload = roleService.updateRoleWithPermissions(rolePayload.getRoleid(), rolePayload);

        return updatedRolePayload.map(payload -> 
            ResponseEntity.status(HttpStatus.OK).body(payload)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    	
    @DeleteMapping("/deleteRole")
    public ResponseEntity<String> deleteRole(@RequestParam String roleId) {
        boolean status = roleService.deleteRole(roleId);
        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body("Role and associated permissions deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete role or role is not inactive");
        }
    }
}
