package com.springboot.manageroles.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.springboot.manageroles.service.RoleColumnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class RoleColumnControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleColumnService roleColumnService;

    @InjectMocks
    private RoleColumnController roleColumnController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleColumnController).build();
    }

    @Test
    void testGetReadableColumnsByFunction_Success() throws Exception {
        String function = "testFunction";
        Set<String> columns = new HashSet<>(Arrays.asList("column1", "column2"));
        when(roleColumnService.getReadableColumnsForFunction(function, null)).thenReturn(columns);

        mockMvc.perform(get("/criteria/getReadableColumnsByFunction")
                .param("function", function))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"column1\", \"column2\"]"));
    }

    @Test
    void testGetReadableColumnsByFunction_BadRequest() throws Exception {
        String function = "invalidFunction";
        when(roleColumnService.getReadableColumnsForFunction(function, null)).thenThrow(new IllegalArgumentException("Invalid function"));

        mockMvc.perform(get("/criteria/getReadableColumnsByFunction")
                .param("function", function))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid function"));
    }

    @Test
    void testSetExcludedFields_Success() throws Exception {
        String tableName = "testTable";
        List<String> fields = Arrays.asList("field1", "field2");
        String requestBody = "[\"field1\", \"field2\"]";

        doNothing().when(roleColumnService).setExcludedFields(eq(tableName), eq(fields));

        mockMvc.perform(post("/criteria/setExclude")
                .param("tableName", tableName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Excluded fields updated successfully."));
    }

    @Test
    void testGetAllFieldsFromTable_Success() throws Exception {
        String tableName = "testTable";
        Set<String> fields = new HashSet<>(Arrays.asList("field1", "field2"));
        when(roleColumnService.getAllFieldsFromTable(tableName)).thenReturn(fields);

        mockMvc.perform(get("/criteria/getAllFields")
                .param("tableName", tableName))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"field1\", \"field2\"]"));
    }

    @Test
    void testGetAllFieldsFromTable_InternalServerError() throws Exception {
        String tableName = "testTable";
        when(roleColumnService.getAllFieldsFromTable(tableName)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/criteria/getAllFields")
                .param("tableName", tableName))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while fetching fields for table: testTable"));
    }
}
