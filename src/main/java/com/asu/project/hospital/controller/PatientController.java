package com.asu.project.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asu.project.hospital.entity.PatientInformation;
import com.asu.project.hospital.service.PatientService;

@Controller
@CrossOrigin
@RequestMapping("/patient")
public class PatientController {
	
	private final PatientService patientService;
	
	 @Autowired
	    public PatientController(PatientService patientService)
	    {
	        this.patientService=patientService;
	    }
	
	@PostMapping(path="/addpatientinfo", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addPatientInformation(@RequestBody PatientInformation patientInformation){
		patientService.addPatientInformation(patientInformation);
		return new ResponseEntity<String>("Updated Information successful", HttpStatus.OK);
	}
	

}
