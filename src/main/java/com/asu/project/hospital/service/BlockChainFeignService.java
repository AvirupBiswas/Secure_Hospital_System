package com.asu.project.hospital.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.asu.project.hospital.model.BlockChainDiagnosisObject;

@FeignClient(name="blockchainDiagnosisService", url="http://52.53.242.26:8080", path="/api/add_report")
public interface BlockChainFeignService {
	
	@PostMapping
	String addDiagnosisToBlockChain(BlockChainDiagnosisObject blockchainObject);
	

}
