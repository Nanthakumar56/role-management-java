package com.springboot.manageroles.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.manageroles.dto.AdvCriteriaDTO;
import com.springboot.manageroles.dto.PermissionDTO;
import com.springboot.manageroles.dto.RoleDTO;
import com.springboot.manageroles.entity.AdvCriteria;
import com.springboot.manageroles.entity.Permissions;
import com.springboot.manageroles.entity.Roles;
import com.springboot.manageroles.repository.AdvCriteriaRepository;
import com.springboot.manageroles.repository.PermissionsRepository;
import com.springboot.manageroles.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private AdvCriteriaRepository advCriteriaRepository;

    public String createRole(RoleDTO rolePayload) {
    	
    	 Optional<Roles> existingRole = roleRepository.getByRolename(rolePayload.getRoleName());

    	    if (existingRole.isPresent()) {
    	        return "Role with the same name already exists."; 
    	    }

        try {
            Roles role = new Roles();
            role.setrolename(rolePayload.getRoleName());
            role.setroledescription(rolePayload.getRoleDescription());
            role.setStatus("Active");
            role.setCreated_at(LocalDateTime.now());
            role.setUsersAssigned(0);
            Roles savedRole = roleRepository.save(role);

            for (PermissionDTO permissionDTO : rolePayload.getPermissions()) {
                String functionality = permissionDTO.getFunctionality();

                Permissions permission = new Permissions();
                permission.setRoleid(savedRole.getRoleid());
                permission.setFunctionality(functionality);
                permission.setIsread(permissionDTO.isIsread());
                permission.setIscreate(permissionDTO.isIscreate());
                permission.setIsedit(permissionDTO.isIsedit());
                permission.setIsdelete(permissionDTO.isIsdelete());
                permission.setCreated_at(LocalDateTime.now());
                Permissions savedPermission = permissionsRepository.save(permission);

                List<AdvCriteria> criteriaList = permissionDTO.getCriteria().stream().map(criteriaDTO -> {
                    AdvCriteria criteria = new AdvCriteria();
                    criteria.setRoleid(savedRole.getRoleid());
                    criteria.setPermissionid(savedPermission.getId());
                    criteria.setFunction(savedPermission.getFunctionality());
                    criteria.setCriterianame(criteriaDTO.getCriterianame());
                    criteria.setOperation(criteriaDTO.getOperation());
                    criteria.setCriteriavalue(criteriaDTO.getCriteriavalue());
                    return criteria;
                }).collect(Collectors.toList());

                advCriteriaRepository.saveAll(criteriaList);
            }
            return "Role created successfully.";
        } catch (DataIntegrityViolationException e) {
            return "Role with the same name already exists.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create role.";
        }
    }


    // GET ALL ROLES
    public List<RoleDTO> getAllRoles() {
        List<Roles> roles = roleRepository.findAll();
        List<Permissions> permissions = permissionsRepository.findAll();
        List<AdvCriteria> criteria = advCriteriaRepository.findAll();

        Map<String, List<Permissions>> permissionsByRole = permissions.stream()
                .collect(Collectors.groupingBy(Permissions::getRoleid));

        Map<String, List<AdvCriteria>> criteriaByPermission = criteria.stream()
                .collect(Collectors.groupingBy(AdvCriteria::getPermissionid));

        return roles.stream().map(role -> {
            List<PermissionDTO> permissionDTOs = permissionsByRole.getOrDefault(role.getRoleid(), Collections.emptyList())
                    .stream()
                    .map(permission -> {
                        List<AdvCriteriaDTO> criteriaDTOs = criteriaByPermission
                                .getOrDefault(permission.getId(), Collections.emptyList())
                                .stream()
                                .map(criterion -> new AdvCriteriaDTO(
                                        criterion.getAdvcid(),
                                        criterion.getPermissionid(),
                                        criterion.getRoleid(),
                                        criterion.getCriterianame(),
                                        criterion.getOperation(),
                                        criterion.getCriteriavalue(),
                                        criterion.getFunction()
                                ))
                                .collect(Collectors.toList());

                        return new PermissionDTO(
                                permission.getId(),
                                permission.getRoleid(),
                                permission.getFunctionality(),
                                permission.isIsread(),
                                permission.isIsedit(),
                                permission.isIscreate(),
                                permission.isIsdelete(),
                                permission.getCreated_at(),
                                criteriaDTOs
                        );
                    })
                    .collect(Collectors.toList());

            return new RoleDTO(
                    role.getRoleid(),
                    role.getrolename(),
                    role.getroledescription(),
                    role.getStatus(),
                    role.getCreated_at(),
                    role.getUsersAssigned(),
                    permissionDTOs
            );
        }).collect(Collectors.toList());
    }
    
    
    // GET ROLE BY ID
    public RoleDTO getRoleByName(String rolename) {
        Optional<Roles> roleOpt = roleRepository.getByRolename(rolename);
        if (roleOpt.isEmpty()) {
            throw new RuntimeException("Role not found.");
        }

        Roles role = roleOpt.get();

        List<Permissions> permissions = permissionsRepository.findByRoleid(role.getRoleid());

        List<AdvCriteria> allCriteria = advCriteriaRepository.findByRoleid(role.getRoleid());

        Map<String, List<AdvCriteria>> criteriaByPermission = allCriteria.stream()
                .collect(Collectors.groupingBy(AdvCriteria::getPermissionid));

        List<PermissionDTO> permissionDTOs = permissions.stream()
                .map(permission -> {
                    List<AdvCriteriaDTO> criteriaDTOs = criteriaByPermission
                            .getOrDefault(permission.getId(), Collections.emptyList())
                            .stream()
                            .map(criterion -> new AdvCriteriaDTO(
                                    criterion.getAdvcid(),
                                    criterion.getPermissionid(),
                                    criterion.getRoleid(),
                                    criterion.getCriterianame(),
                                    criterion.getOperation(),
                                    criterion.getCriteriavalue(),
                                    criterion.getFunction()
                            ))
                            .collect(Collectors.toList());

                    return new PermissionDTO(
                            permission.getId(),
                            permission.getRoleid(),
                            permission.getFunctionality(),
                            permission.isIsread(),
                            permission.isIsedit(),
                            permission.isIscreate(),
                            permission.isIsdelete(),
                            permission.getCreated_at(),
                            criteriaDTOs
                    );
                })
                .collect(Collectors.toList());

        return new RoleDTO(
                role.getRoleid(),
                role.getrolename(),
                role.getroledescription(),
                role.getStatus(),
                role.getCreated_at(),
                role.getUsersAssigned(),
                permissionDTOs
        );
    }
    
    public RoleDTO getRoleById(String roleId) {
        Optional<Roles> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new RuntimeException("Role not found.");
        }

        Roles role = roleOpt.get();

        List<Permissions> permissions = permissionsRepository.findByRoleid(roleId);

        List<AdvCriteria> allCriteria = advCriteriaRepository.findByRoleid(roleId);

        Map<String, List<AdvCriteria>> criteriaByPermission = allCriteria.stream()
                .collect(Collectors.groupingBy(AdvCriteria::getPermissionid));

        List<PermissionDTO> permissionDTOs = permissions.stream()
                .map(permission -> {
                    List<AdvCriteriaDTO> criteriaDTOs = criteriaByPermission
                            .getOrDefault(permission.getId(), Collections.emptyList())
                            .stream()
                            .map(criterion -> new AdvCriteriaDTO(
                                    criterion.getAdvcid(),
                                    criterion.getPermissionid(),
                                    criterion.getRoleid(),
                                    criterion.getCriterianame(),
                                    criterion.getOperation(),
                                    criterion.getCriteriavalue(),
                                    criterion.getFunction()
                            ))
                            .collect(Collectors.toList());

                    return new PermissionDTO(
                            permission.getId(),
                            permission.getRoleid(),
                            permission.getFunctionality(),
                            permission.isIsread(),
                            permission.isIsedit(),
                            permission.isIscreate(),
                            permission.isIsdelete(),
                            permission.getCreated_at(),
                            criteriaDTOs
                    );
                })
                .collect(Collectors.toList());

        return new RoleDTO(
                role.getRoleid(),
                role.getrolename(),
                role.getroledescription(),
                role.getStatus(),
                role.getCreated_at(),
                role.getUsersAssigned(),
                permissionDTOs
        );
    }


    //UPDATE ROLE
    @Transactional
    public String updateRole(String roleId, RoleDTO rolePayload) {
        Optional<Roles> existingRoleOpt = roleRepository.findById(roleId);
        if (existingRoleOpt.isEmpty()) {
            return "Role not found.";
        }

        Roles existingRole = existingRoleOpt.get();

        existingRole.setrolename(rolePayload.getRoleName());
        existingRole.setroledescription(rolePayload.getRoleDescription());
        existingRole.setUpdated_at(LocalDateTime.now());
        roleRepository.save(existingRole);

        List<Permissions> existingPermissions = permissionsRepository.findByRoleid(roleId);
        Map<String, Permissions> existingPermissionsMap = existingPermissions.stream()
                .collect(Collectors.toMap(Permissions::getFunctionality, perm -> perm));

        for (PermissionDTO permissionDTO : rolePayload.getPermissions()) {
            String functionality = permissionDTO.getFunctionality();

            Permissions permission = existingPermissionsMap.get(functionality);
            if (permission == null) {
                permission = new Permissions();
                permission.setRoleid(roleId);
                permission.setFunctionality(functionality);
            }

            permission.setIsread(permissionDTO.isIsread());
            permission.setIscreate(permissionDTO.isIscreate());
            permission.setIsedit(permissionDTO.isIsedit());
            permission.setIsdelete(permissionDTO.isIsdelete());
            Permissions savedPermission = permissionsRepository.save(permission);

            advCriteriaRepository.deleteByPermissionid(savedPermission.getId());

            List<AdvCriteria> criteriaList = permissionDTO.getCriteria().stream()
                    .map(criteriaDTO -> {
                        AdvCriteria criteria = new AdvCriteria();
                        criteria.setRoleid(roleId);
                        criteria.setPermissionid(savedPermission.getId());
                        criteria.setFunction(savedPermission.getFunctionality());
                        criteria.setCriterianame(criteriaDTO.getCriterianame());
                        criteria.setOperation(criteriaDTO.getOperation());
                        criteria.setCriteriavalue(criteriaDTO.getCriteriavalue());
                        return criteria;
                    })
                    .collect(Collectors.toList());

            advCriteriaRepository.saveAll(criteriaList);
        }

        Set<String> functionalitiesInPayload = rolePayload.getPermissions().stream()
                .map(PermissionDTO::getFunctionality)
                .collect(Collectors.toSet());

        existingPermissions.stream()
                .filter(permission -> !functionalitiesInPayload.contains(permission.getFunctionality()))
                .forEach(permission -> {
                    advCriteriaRepository.deleteByPermissionid(permission.getId());
                    permissionsRepository.delete(permission);
                });

        return "Role updated successfully.";
    }


    // DELETE ROLE
    @Transactional
    public String deleteRole(String roleId) {
        Optional<Roles> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            return "Role not found.";
        }

        Roles role = roleOpt.get();

        List<Permissions> permissions = permissionsRepository.findByRoleid(roleId);

        for (Permissions permission : permissions) {
            advCriteriaRepository.deleteByPermissionid(permission.getId());
        }

        permissionsRepository.deleteByRoleid(roleId);

        roleRepository.delete(role);

        return "Role deleted successfully.";
    }
    
    public boolean updateRoleUser(String rolename)
    {
    	try {

        	Optional<Roles> role = roleRepository.getByRolename(rolename);
        	if (!role.isPresent()) {
        	    return false;
        	}
        	Roles roledata = role.get();

        	roledata.setUsersAssigned(roledata.getUsersAssigned()+1);

        	roleRepository.save(roledata);
        	return true;
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    	
    }
    public boolean updateRoleUserRemove(String rolename)
    {
    	try {
    		
    		Optional<Roles> role = roleRepository.getByRolename(rolename);
    		if (!role.isPresent()) {
    		    return false;
    		}
    		Roles roledata = role.get();

        	roledata.setUsersAssigned(roledata.getUsersAssigned()-1);
        	
        	roleRepository.save(roledata);
        	return true;
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    	
    }
}
