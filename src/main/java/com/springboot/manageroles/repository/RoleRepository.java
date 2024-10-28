package com.springboot.manageroles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.manageroles.entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String>   {

}
