package com.springboot.manageroles.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.springboot.manageroles.config.FunctionTableMapper;
import com.springboot.manageroles.entity.ExcludeFields;
import com.springboot.manageroles.repository.ExcludeFieldsRepository;

@Service
public class RoleColumnService {

    private final DataSource dataSource;
    private final ExcludeFieldsRepository excludeFieldsRepository;

    public RoleColumnService(DataSource dataSource, ExcludeFieldsRepository excludeFieldsRepository) {
        this.dataSource = dataSource;
        this.excludeFieldsRepository = excludeFieldsRepository;
    }

    public Set<String> getReadableColumnsForFunction(String function, String searchString) {
        String tableName = FunctionTableMapper.FUNCTION_TO_TABLE_MAP.get(function);
        if (tableName == null) {
            throw new IllegalArgumentException("No table mapping found for the given function.");
        }

        Set<String> functionColumns = fetchColumnNamesWithTable(tableName);
        Set<String> userColumns = fetchColumnNamesWithTable("users");

        functionColumns.addAll(userColumns);

        if (function.equals("module") || function.equals("task") || function.equals("stories")) {
            Set<String> projectColumns = fetchColumnNamesWithTable("projects");
            functionColumns.addAll(projectColumns);
        }

        functionColumns = excludeUnwantedFields(functionColumns);

        if (searchString != null && !searchString.isEmpty()) {
            functionColumns = functionColumns.stream()
                    .filter(column -> {
                        String[] parts = column.split("\\."); 
                        String columnName = parts[1]; 
                        return columnName.contains(searchString); 
                    })
                    .collect(Collectors.toSet());
        }

        return functionColumns.stream()
                .map(this::convertToReadableFormat)
                .collect(Collectors.toSet());
    }

    private Set<String> fetchColumnNamesWithTable(String tableName) {
        Set<String> columnNames = new HashSet<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                while (columns.next()) {
                    columnNames.add(tableName + "." + columns.getString("COLUMN_NAME"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching column names from database: " + e.getMessage(), e);
        }
        return columnNames;
    }

    private Set<String> excludeUnwantedFields(Set<String> columnNames) {
        List<ExcludeFields> excludedFields = excludeFieldsRepository.findAll();

        return columnNames.stream()
                .filter(column -> {
                    String[] parts = column.split("\\.");
                    String tableName = parts[0];
                    String columnName = parts[1];
                    return excludedFields.stream().noneMatch(
                            exclude -> exclude.getTablename().equalsIgnoreCase(tableName)
                                    && exclude.getFieldname().equalsIgnoreCase(columnName));
                })
                .collect(Collectors.toSet());
    }

    private String convertToReadableFormat(String columnNameWithTable) {
        String[] parts = columnNameWithTable.split("\\.");
        String tableName = capitalize(parts[0]); 
        String columnName = parts[1];

        String readableColumnName = capitalizeWords(columnName.replace("_", " "));
        return tableName + " : " + readableColumnName;
    }

    private String capitalizeWords(String input) {
        return Arrays.stream(input.split(" ")) 
                     .map(word -> word.length() > 0 ? 
                         Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase() : word)
                     .collect(Collectors.joining(" "));
    }

    private String capitalize(String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }
    
    public void setExcludedFields(String tableName, List<String> fields) {
        excludeFieldsRepository.deleteByTablename(tableName);

        List<ExcludeFields> newExcludedFields = fields.stream()
                .map(field -> new ExcludeFields(null, tableName, field))
                .toList();

        excludeFieldsRepository.saveAll(newExcludedFields);
    }
    
    public Set<String> getAllFieldsFromTable(String tableName) {
        Set<String> columnNames = new HashSet<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                while (columns.next()) {
                    columnNames.add(columns.getString("COLUMN_NAME"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching column names for table: " + tableName, e);
        }
        return columnNames;
    }

}
