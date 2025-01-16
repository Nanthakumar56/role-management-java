package com.springboot.manageroles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.manageroles.entity.AdvCriteria;

@Repository
public interface AdvCriteriaRepository extends JpaRepository<AdvCriteria, String> {

	void deleteByPermissionid(String id);

	List<AdvCriteria> findByPermissionid(String id);

	List<AdvCriteria> findByRoleid(String roleId);

}
