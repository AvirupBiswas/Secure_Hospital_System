package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.SignInHistory;

public interface SignInHistoryRepository extends JpaRepository<SignInHistory, Integer>{

}
