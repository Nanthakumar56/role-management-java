package com.springboot.manageroles.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.springboot.manageroles.dto.RoleDTO;
import com.springboot.manageroles.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    void testCreateRole_Success() throws Exception {
        RoleDTO role = new RoleDTO("1", "Admin", "Admin Role", null, null, 0, null);
        when(roleService.createRole(any(RoleDTO.class))).thenReturn("Role Created Successfully");

        mockMvc.perform(post("/roles/newRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\":\"1\", \"roleName\":\"Admin\", \"description\":\"Admin Role\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role Created Successfully"));
    }

    @Test
    void testGetAllRoles_Success() throws Exception {
        List<RoleDTO> roles = Arrays.asList(new RoleDTO("1", "Admin", "Admin Role", null, null, 0, null));
        when(roleService.getAllRoles()).thenReturn(roles);

        String expectedJson = "[{"
                + "\"roleId\":\"1\","
                + "\"roleName\":\"Admin\","
                + "\"roleDescription\":\"Admin Role\""
                + "}]";

        MvcResult result = mockMvc.perform(get("/roles/getallroles"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        System.out.println("Actual JSON Response: " + actualJson); // Debugging line

        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
    }

    @Test
    void testGetRole_Success() throws Exception {
        RoleDTO role = new RoleDTO("1", "Admin", "Admin Role", null, null, 0, null);
        when(roleService.getRoleById("1")).thenReturn(role);

        MvcResult result = mockMvc.perform(get("/roles/getRole").param("roleId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        System.out.println("Actual JSON Response: " + actualJson); // Debugging line

        String expectedJson = "{"
                + "\"roleId\":\"1\","
                + "\"roleName\":\"Admin\","
                + "\"roleDescription\":\"Admin Role\"," 
                + "\"status\":null,"
                + "\"created_at\":null,"
                + "\"permissions\":null,"
                + "\"usersAssigned\":0"
                + "}";

        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
    }

    @Test
    void testDeleteRole_Success() throws Exception {
        when(roleService.deleteRole("1")).thenReturn("Role Deleted Successfully");

        mockMvc.perform(delete("/roles/deleteRole").param("roleId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role Deleted Successfully"));
    }
}
