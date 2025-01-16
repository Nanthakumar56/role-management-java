package com.springboot.manageroles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.manageroles.entity.ExcludeFields;

@Repository
public interface ExcludeFieldsRepository extends JpaRepository<ExcludeFields, String> {

	void deleteByTablename(String tableName);

}
