package com.springboot.manageroles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.manageroles.dto.RoleDTO;
import com.springboot.manageroles.service.RoleService;

@Controller
@RequestMapping("/roles")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/newRole")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO role) {
        String rolePayload = roleService.createRole(role);

        if (rolePayload != null) {
            return ResponseEntity.status(HttpStatus.OK).body(rolePayload);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rolePayload);
        }
    }

	@GetMapping("/getallroles")
	public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> rolePayloadList = roleService.getAllRoles();

        if (!rolePayloadList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(rolePayloadList);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

	@GetMapping("/getRole")
	public ResponseEntity<?> getRole(@RequestParam String roleId) {
	    try {
	        RoleDTO rolePayload = roleService.getRoleById(roleId);

	        if (rolePayload == null) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        }

	        return ResponseEntity.status(HttpStatus.OK).body(rolePayload);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the role.");
	    }
	}
	
	@GetMapping("/getRoleByName")
	public ResponseEntity<?> getRolebyName(@RequestParam String rolename) {
	    try {
	        RoleDTO rolePayload = roleService.getRoleByName(rolename);

	        if (rolePayload == null) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        }

	        return ResponseEntity.status(HttpStatus.OK).body(rolePayload);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the role.");
	    }
	}

    
    @PutMapping("/update")
    public ResponseEntity<?> updateRoleWithPermissions(
        @RequestBody RoleDTO rolePayload
    ) {
        String updatedRolePayload = roleService.updateRole(rolePayload.getRoleId(), rolePayload);

        if(updatedRolePayload != null)
        {
        	return  ResponseEntity.status(HttpStatus.OK).body(updatedRolePayload);
        }
        else {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role updation failure");
        }
    }
    
    @PutMapping("/updateRoleUsers")
    public ResponseEntity<?> updateRoleUsers(
        @RequestParam String rolename
    ) {
        boolean response = roleService.updateRoleUser(rolename);

        if(response)
        {
        	return  ResponseEntity.status(HttpStatus.OK).body("Success");
        }
        else {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role updation failure");
        }
    }
    
    @PutMapping("/updateRoleUsersRemove")
    public ResponseEntity<?> updateRoleUsersRemoe(
        @RequestParam String rolename
    ) {

        boolean response = roleService.updateRoleUserRemove(rolename);

        if(response)
        {
        	return  ResponseEntity.status(HttpStatus.OK).body("Success");
        }
        else {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role updation failure");
        }
    }
    	
    @DeleteMapping("/deleteRole")
    public ResponseEntity<String> deleteRole(@RequestParam String roleId) {
        String status = roleService.deleteRole(roleId);
        if (status != null) {
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete role or role is not inactive");
        }
    }
}
