package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.AdminDecisionForUser;

public interface AdminDecisionForUserRepository extends JpaRepository<AdminDecisionForUser, Long> {
}