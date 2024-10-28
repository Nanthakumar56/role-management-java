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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.manageroles.entity.Roles;
import com.springboot.manageroles.repository.RoleRepository;
import com.springboot.manageroles.service.RoleService;

@Controller
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/newRole")
    public ResponseEntity<String> createRole(@RequestBody Roles roles) {
        boolean status = roleService.createRole(roles);
        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body("Role created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create role");
        }
    }
	@GetMapping("/getrole")
    public ResponseEntity<List<Roles>> getAllRoles() {  
        List<Roles> roleList = roleService.getAllRoles();
        if (!roleList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(roleList);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
        }
    }
	@GetMapping("/{roleId}")
	public ResponseEntity<Optional<Roles>> getRole(@PathVariable String roleId)
	{
		Optional<Roles> roleData = roleService.getRoleById(roleId);
		if(!roleData.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(roleData);
		}
		else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	@PostMapping("/deactivate")
	public ResponseEntity<String> deactivateRole(@RequestParam String roleId){
		boolean status = roleService.deactivateRole(roleId);
        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body("Role updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update role");
        }
	}
	@PostMapping("/activate")
	public ResponseEntity<String> activateRole(@RequestParam String roleId){
		boolean status = roleService.activateRole(roleId);
        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body("Role updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update role");
        }
	}
	@DeleteMapping("/deleteRole")
	public ResponseEntity<String> deleteRole(@RequestParam String roleId){
		boolean status = roleService.deleteRole(roleId);
        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body("Role deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete role");
        }
	}
}
