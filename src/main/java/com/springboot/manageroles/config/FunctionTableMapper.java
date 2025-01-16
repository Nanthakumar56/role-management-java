package com.springboot.manageroles.config;

import java.util.Map;

public class FunctionTableMapper {
    public static final Map<String, String> FUNCTION_TO_TABLE_MAP = Map.of(
        "project", "projects",
        "module", "modules",
        "task", "tasks",
        "stories", "stories",
        "templates" , "templates",
        "milestones" , "milestones",
        "automations", "automations"
        );
}


