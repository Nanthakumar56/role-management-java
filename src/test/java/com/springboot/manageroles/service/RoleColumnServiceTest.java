package com.springboot.manageroles.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import com.springboot.manageroles.entity.ExcludeFields;
import com.springboot.manageroles.repository.ExcludeFieldsRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Prevents unnecessary stubbing errors
class RoleColumnServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private ExcludeFieldsRepository excludeFieldsRepository;

    @InjectMocks
    private RoleColumnService roleColumnService;

    @Mock
    private Connection connection;

    @Mock
    private DatabaseMetaData metaData;

    @Mock
    private ResultSet resultSet1, resultSet2; // Separate result sets for different tables

    @Mock
    private ResultSet resultSetModules, resultSetUsers, resultSetProjects;

    @BeforeEach
    void setUp() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);

        // Ensure metaData.getColumns() never returns null
        when(metaData.getColumns(any(), any(), eq("modules"), any())).thenReturn(resultSetModules);
        when(metaData.getColumns(any(), any(), eq("users"), any())).thenReturn(resultSetUsers);
        when(metaData.getColumns(any(), any(), eq("projects"), any())).thenReturn(resultSetProjects);

        // Mock module columns
        when(resultSetModules.next()).thenReturn(true, true, false);
        when(resultSetModules.getString("COLUMN_NAME")).thenReturn("module_name", "module_status");

        // Mock user columns
        when(resultSetUsers.next()).thenReturn(true, true, false);
        when(resultSetUsers.getString("COLUMN_NAME")).thenReturn("user_name", "user_email");

        // Mock project columns
        when(resultSetProjects.next()).thenReturn(true, false);
        when(resultSetProjects.getString("COLUMN_NAME")).thenReturn("project_name");

        // Exclude fields
        when(excludeFieldsRepository.findAll()).thenReturn(List.of(
            new ExcludeFields("1L", "modules", "module_status")
        ));
    }    @Test
    void testGetReadableColumnsForFunction_Success() throws Exception {
        Set<String> readableColumns = roleColumnService.getReadableColumnsForFunction("module", null);

        Set<String> expectedColumns = Set.of(
            "Modules : Module Name",
            "Users : User Name",
            "Users : User Email",
            "Projects : Project Name"
        );

        assertEquals(expectedColumns, readableColumns);
    }

    @Test
    void testGetReadableColumnsForFunction_NoTableMapping() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            roleColumnService.getReadableColumnsForFunction("invalid_function", null)
        );

        assertEquals("No table mapping found for the given function.", exception.getMessage());
    }

    @Test
    void testSetExcludedFields() {
        doNothing().when(excludeFieldsRepository).deleteByTablename("modules");

        List<String> fieldsToExclude = List.of("module_status", "module_priority");
        roleColumnService.setExcludedFields("modules", fieldsToExclude);

        verify(excludeFieldsRepository, times(1)).deleteByTablename("modules");
        verify(excludeFieldsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testGetAllFieldsFromTable_Success() throws Exception {
        when(metaData.getColumns(null, null, "modules", null)).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true, true, false);
        when(resultSet1.getString("COLUMN_NAME")).thenReturn("module_name", "module_status");

        Set<String> actualFields = roleColumnService.getAllFieldsFromTable("modules");
        Set<String> expectedFields = Set.of("module_name", "module_status");

        assertEquals(expectedFields, actualFields);
    }
}
