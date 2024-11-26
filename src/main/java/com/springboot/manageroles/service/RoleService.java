package com.springboot.manageroles.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.manageroles.dto.RolePayload;
import com.springboot.manageroles.entity.Permissions;
import com.springboot.manageroles.entity.Roles;
import com.springboot.manageroles.repository.PermissionsRepository;
import com.springboot.manageroles.repository.RoleRepository;

@Service
public class RoleService 
{
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionsRepository permissionRepository;
	
	public String createRole(RolePayload rolePayload) {
	    try {
	        Roles role = new Roles();
	        role.setrolename(rolePayload.getRolename());
	        role.setroledescription(rolePayload.getRoledescription());
	        role.setStatus("Active");
	        LocalDateTime now = LocalDateTime.now();
	        role.setCreated_at(now);

	        Roles savedRole = roleRepository.save(role);

	        Map<String, RolePayload.PermissionFlags> permissionsMap = new HashMap<>();

	        for (Map.Entry<String, RolePayload.PermissionFlags> entry : rolePayload.getPermissions().entrySet()) {
	            String functionality = entry.getKey();
	            RolePayload.PermissionFlags flags = entry.getValue();

	            Permissions permission = new Permissions();
	            permission.setRoleid(savedRole.getRoleid());
	            permission.setFunctionality(functionality);
	            permission.setIsread(flags.isRead());
	            permission.setIscreate(flags.isCreate());
	            permission.setIsedit(flags.isEdit());
	            permission.setIsdelete(flags.isDelete());
	            permission.setCreated_at(now);

	            permissionRepository.save(permission);

	            permissionsMap.put(functionality, flags);
	        }

	        RolePayload responsePayload = new RolePayload(
	            savedRole.getRoleid(),
	            savedRole.getrolename(),
	            savedRole.getroledescription(),
	            savedRole.getStatus(),
	            savedRole.getCreated_at(),
	            savedRole.getUpdated_at(),
	            permissionsMap
	        );

	        return "Success";

	    } catch (DataIntegrityViolationException e) {
	        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
	            System.err.println("Role already exists with the same name.");
	            System.err.println("Role with name '" + rolePayload.getRolename() + "' already exists.");
	            return ("Role with name '" + rolePayload.getRolename() + "' already exists.");
	        }
	        return e.getLocalizedMessage();
	    } 
	}

	 public List<RolePayload> getAllRolePayloads() {
	        List<Permissions> allPermissions = permissionRepository.findAll();
	        Map<String, Map<String, RolePayload.PermissionFlags>> permissionsMap = allPermissions.stream()
	            .collect(Collectors.groupingBy(
	                Permissions::getRoleid,
	                Collectors.toMap(
	                    Permissions::getFunctionality,
	                    perm -> new RolePayload.PermissionFlags(
	                        perm.isIsread(),
	                        perm.isIsedit(),
	                        perm.isIscreate(),
	                        perm.isIsdelete()
	                    )
	                )
	            ));

	        return roleRepository.findAll().stream()
	            .map(role -> new RolePayload(
	                role.getRoleid(),
	                role.getrolename(),
	                role.getroledescription(),
	                role.getStatus(),
	                role.getCreated_at(),
	                role.getUpdated_at(),
	                permissionsMap.getOrDefault(role.getRoleid(), Collections.emptyMap())
	            ))
	            .collect(Collectors.toList());
	    }

	    public Optional<RolePayload> getRolePayloadById(String roleId) {
	        return roleRepository.findById(roleId)
	            .map(role -> {
	                Map<String, RolePayload.PermissionFlags> permissions = permissionRepository.findByRoleid(roleId).stream()
	                    .collect(Collectors.toMap(
	                        Permissions::getFunctionality,
	                        perm -> new RolePayload.PermissionFlags(
	                            perm.isIsread(),
	                            perm.isIsedit(),
	                            perm.isIscreate(),
	                            perm.isIsdelete()
	                        )
	                    ));

	                return new RolePayload(role.getRoleid(), role.getrolename(), role.getroledescription(),role.getStatus(),role.getCreated_at(),role.getUpdated_at(), permissions);
	            });
	    }
	
	
	@Transactional
    public Optional<RolePayload> updateRoleWithPermissions(String roleId, RolePayload rolePayload) {
        // Fetch the existing role by ID
        Optional<Roles> existingRoleOpt = roleRepository.findById(roleId);

        if (existingRoleOpt.isEmpty()) {
            return Optional.empty();
        }

        Roles existingRole = existingRoleOpt.get();
        existingRole.setrolename(rolePayload.getRolename());
        existingRole.setroledescription(rolePayload.getRoledescription());
        existingRole.setStatus(rolePayload.getStatus());
        existingRole.setUpdated_at(LocalDateTime.now());

        roleRepository.save(existingRole);

        Map<String, RolePayload.PermissionFlags> newPermissionsMap = rolePayload.getPermissions();
        List<Permissions> existingPermissions = permissionRepository.findByRoleid(roleId);

        // Map existing permissions by functionality for easy lookup
        Map<String, Permissions> existingPermissionsMap = existingPermissions.stream()
            .collect(Collectors.toMap(Permissions::getFunctionality, perm -> perm));

        List<Permissions> updatedPermissions = new ArrayList<>();

        for (Map.Entry<String, RolePayload.PermissionFlags> entry : newPermissionsMap.entrySet()) {
            String functionality = entry.getKey();
            RolePayload.PermissionFlags flags = entry.getValue();

            Permissions permission = existingPermissionsMap.getOrDefault(functionality, new Permissions());
            permission.setRoleid(roleId);
            permission.setFunctionality(functionality);
            permission.setIsread(flags.isRead());
            permission.setIscreate(flags.isCreate());
            permission.setIsedit(flags.isEdit());
            permission.setIsdelete(flags.isDelete());

            updatedPermissions.add(permission);
        }

        // Save all updated permissions
        permissionRepository.saveAll(updatedPermissions);

        List<Permissions> permissionsToDelete = existingPermissions.stream()
            .filter(perm -> !newPermissionsMap.containsKey(perm.getFunctionality()))
            .collect(Collectors.toList());
        permissionRepository.deleteAll(permissionsToDelete);

        return Optional.of(new RolePayload(
            existingRole.getRoleid(),
            existingRole.getrolename(),
            existingRole.getroledescription(),
            existingRole.getStatus(),
            existingRole.getCreated_at(),
            existingRole.getUpdated_at(),
            newPermissionsMap
        ));
    }
	
	@Transactional
    public boolean deleteRole(String roleId) {
        try {
            // Check if the role exists
            Optional<Roles> existingRole = roleRepository.findById(roleId);
            if (existingRole.isPresent()) {
                Roles role = existingRole.get();

                // Check if the role status is "Inactive"
                if ("Inactive".equals(role.getStatus())) {
                    // Delete all permissions associated with the role
                    permissionRepository.deleteByRoleid(roleId);

                    // Delete the role itself
                    roleRepository.deleteById(roleId);

                    return true; // Successful deletion
                } else {
                    System.out.println("Role status is not Inactive.");
                    return false; // Role status is not Inactive, do not delete
                }
            } else {
                System.out.println("Role not found with ID: " + roleId);
                return false; // Role not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Exception occurred during deletion
        }
    }
}
