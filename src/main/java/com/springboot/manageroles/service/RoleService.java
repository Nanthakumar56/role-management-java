package com.springboot.manageroles.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.manageroles.entity.Roles;
import com.springboot.manageroles.repository.RoleRepository;

@Service
public class RoleService 
{
	@Autowired
	private RoleRepository roleRepository;
	
	public boolean createRole(Roles role) {
	    try {
	        LocalDateTime now = LocalDateTime.now();
	        role.setCreated_at(now); 
	        role.setStatus("Active");
	        roleRepository.save(role);
	        return true; 
	    } catch (Exception e) {
	        System.err.println("Error creating role: " + e.getMessage());
	        return false; 
	    }
	}

	public List<Roles> getAllRoles()
	{
		return roleRepository.findAll();
	}
	
	public Optional<Roles> getRoleById(String roleId) {
		return roleRepository.findById(roleId);
	}
	public boolean deactivateRole(String roleId) {
	    try {
	        Optional<Roles> existingRole = roleRepository.findById(roleId);
	        if(existingRole.isPresent()) {
		        LocalDateTime now = LocalDateTime.now();
	            Roles role = existingRole.get();
	            role.setUpdated_at(now);
	            role.setStatus("Inactive"); 
	            roleRepository.save(role);
	            return true; 
	        } else {
	            System.out.println("Role not found with ID: " + roleId);
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean activateRole(String roleId) {
	    try {
	        Optional<Roles> existingRole = roleRepository.findById(roleId);
	        if(existingRole.isPresent()) {
		        LocalDateTime now = LocalDateTime.now();
	            Roles role = existingRole.get();
	            role.setStatus("Active"); 
	            role.setUpdated_at(now);
	            roleRepository.save(role);
	            return true; 
	        } else {
	            System.out.println("Role not found with ID: " + roleId);
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean deleteRole(String roleId) {
		try {
	        Optional<Roles> existingRole = roleRepository.findById(roleId);
	        if(existingRole.isPresent()) {
	            Roles role = existingRole.get();
	            if(role.getStatus().equals("Inactive"))
	            {
	            	roleRepository.deleteById(roleId);
	            	return true; 
	            }
	            else {
	            	return false;
	            }
	        } else {
	            System.out.println("Role not found with ID: " + roleId);
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
