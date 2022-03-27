package com.asu.project.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.asu.project.hospital.entity.SystemLog;

public interface SystemLogRepository extends PagingAndSortingRepository<SystemLog, String> {
	
	Page<SystemLog> findAll(Pageable requestedPage);
}
