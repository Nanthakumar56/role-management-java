package com.springboot.manageroles.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.manageroles.service.RoleColumnService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/criteria")
public class RoleColumnController {

    private final RoleColumnService roleColumnService;

    public RoleColumnController(RoleColumnService roleColumnService) {
        this.roleColumnService = roleColumnService;
    }
    
    @GetMapping("/getReadableColumnsByFunction")
    public ResponseEntity<?> getReadableColumnsByFunction(@RequestParam String function, @RequestParam(required = false) String searchString) {
        try {
            Set<String> columns = roleColumnService.getReadableColumnsForFunction(function, searchString);
            return ResponseEntity.ok(columns);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while fetching columns.");
        }
    }
    
    @PostMapping("/setExclude")
    public ResponseEntity<?> setExcludedFields(@RequestParam String tableName, @RequestBody List<String> fields) {
        try {
        	roleColumnService.setExcludedFields(tableName, fields);
            return ResponseEntity.ok("Excluded fields updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating excluded fields: " + e.getMessage());
        }
    }
    
    @GetMapping("/getAllFields")
    public ResponseEntity<?> getAllFieldsFromTable(@RequestParam String tableName) {
        try {
            Set<String> fields = roleColumnService.getAllFieldsFromTable(tableName);
            return ResponseEntity.ok(fields);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching fields for table: " + tableName);
        }
    }

}
