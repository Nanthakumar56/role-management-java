package com.springboot.manageroles.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.manageroles.dto.PermissionDTO;
import com.springboot.manageroles.dto.RoleDTO;
import com.springboot.manageroles.entity.Permissions;
import com.springboot.manageroles.entity.Roles;
import com.springboot.manageroles.repository.AdvCriteriaRepository;
import com.springboot.manageroles.repository.PermissionsRepository;
import com.springboot.manageroles.repository.RoleRepository;

@SpringBootTest
@Transactional
@Rollback(true) 
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionsRepository permissionsRepository;

    @Mock
    private AdvCriteriaRepository advCriteriaRepository;

    @InjectMocks
    private RoleService roleService;

    private Roles role;
    private RoleDTO roleDTO;
    private PermissionDTO permissionDTO;
    private Permissions permission;

    @BeforeEach
    void setUp() {
        role = new Roles();
        role.setRoleid("1");
        role.setrolename("Admin");
        role.setroledescription("Administrator role");
        role.setStatus("Active");
        role.setCreated_at(LocalDateTime.now());
        role.setUsersAssigned(0);

        permission = new Permissions();
        permission.setId("101");
        permission.setRoleid("1");
        permission.setFunctionality("Manage Users");
        permission.setIsread(true);
        permission.setIscreate(true);
        permission.setIsedit(true);
        permission.setIsdelete(true);
        permission.setCreated_at(LocalDateTime.now());

        permissionDTO = new PermissionDTO(
            "101", "1", "Manage Users", true, true, true, true, LocalDateTime.now(), new ArrayList<>()
        );

        roleDTO = new RoleDTO(
            "1", "Admin", "Administrator role", "Active", LocalDateTime.now(), 0, List.of(permissionDTO)
        );
    }

    @Test
    void testCreateRole_Success() {
        when(roleRepository.save(any(Roles.class))).thenReturn(role);
        when(permissionsRepository.save(any(Permissions.class))).thenReturn(permission);
        
        String result = roleService.createRole(roleDTO);
        assertEquals("Role created successfully.", result);
    }

    @Test
    void testCreateRole_DuplicateRoleName() {
        when(roleRepository.getByRolename("Admin")).thenReturn(Optional.of(role));

        String result = roleService.createRole(roleDTO);

        assertEquals("Role with the same name already exists.", result);
    }


    @Test
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(permissionsRepository.findAll()).thenReturn(List.of(permission));
        when(advCriteriaRepository.findAll()).thenReturn(new ArrayList<>());
        
        List<RoleDTO> roles = roleService.getAllRoles();
        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getRoleName());
    }

    @Test
    void testGetRoleById_RoleExists() {
        when(roleRepository.findById("1")).thenReturn(Optional.of(role));
        when(permissionsRepository.findByRoleid("1")).thenReturn(List.of(permission));
        when(advCriteriaRepository.findByRoleid("1")).thenReturn(new ArrayList<>());
        
        RoleDTO result = roleService.getRoleById("1");
        assertNotNull(result);
        assertEquals("Admin", result.getRoleName());
    }

    @Test
    void testGetRoleById_RoleNotFound() {
        when(roleRepository.findById("1")).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> roleService.getRoleById("1"));
        assertEquals("Role not found.", exception.getMessage());
    }

    @Test
    void testUpdateRole_RoleExists() {
        when(roleRepository.findById("1")).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Roles.class))).thenReturn(role);
        when(permissionsRepository.findByRoleid("1")).thenReturn(List.of(permission));
        when(permissionsRepository.save(any(Permissions.class))).thenReturn(permission);
        
        String result = roleService.updateRole("1", roleDTO);
        assertEquals("Role updated successfully.", result);
    }

    @Test
    void testUpdateRole_RoleNotFound() {
        when(roleRepository.findById("1")).thenReturn(Optional.empty());
        
        String result = roleService.updateRole("1", roleDTO);
        assertEquals("Role not found.", result);
    }

    @Test
    void testDeleteRole_Success() {
        when(roleRepository.findById("1")).thenReturn(Optional.of(role));
        when(permissionsRepository.findByRoleid("1")).thenReturn(List.of(permission));
        doNothing().when(advCriteriaRepository).deleteByPermissionid(anyString());
        doNothing().when(permissionsRepository).deleteByRoleid(anyString());
        doNothing().when(roleRepository).delete(any(Roles.class));
        
        String result = roleService.deleteRole("1");
        assertEquals("Role deleted successfully.", result);
    }

    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.findById("1")).thenReturn(Optional.empty());
        
        String result = roleService.deleteRole("1");
        assertEquals("Role not found.", result);
    }
}
