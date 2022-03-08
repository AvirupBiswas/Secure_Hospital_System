package com.asu.project.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.User;

public interface AdminDecisionForUserRepository extends JpaRepository<AdminDecisionForUser, Long> {
	
	Optional<AdminDecisionForUser> findByEmail(String username);

	Optional<AdminDecisionForUser> findOneByEmailIgnoreCase(String username);
}