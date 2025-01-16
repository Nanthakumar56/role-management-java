package com.springboot.manageroles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.manageroles.entity.Permissions;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, String> {
    List<Permissions> findByRoleid(String roleId);
    void deleteByRoleid(String roleid);
	Optional<Permissions> findByRoleidAndFunctionality(String roleId, String function);
}
