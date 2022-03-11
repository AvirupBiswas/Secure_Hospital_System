package com.asu.project.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.asu.project.hospital.entity.SignInHistory;

public interface SignInHistoryRepository extends PagingAndSortingRepository<SignInHistory, Integer>{
	
	Page<SignInHistory> findAll(Pageable requestedPage);

}
