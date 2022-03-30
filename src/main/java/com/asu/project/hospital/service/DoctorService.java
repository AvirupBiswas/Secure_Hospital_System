package com.asu.project.hospital.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.repository.DoctorRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.repository.UserRepository;

@Service
public class DoctorService {
	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DiagnosisRepository diagnosisRepository;
	
	@Autowired
	SystemLogRepository systemLogRepository;
	
	public void updateDoctorInfo(Doctor doctor) {
		User user= userService.getLoggedUser();
		doctor.setUser(user);
		doctorRepository.save(doctor);
	}
	
	public void createDiagnosis(Diagnosis diagnosis) {
		SystemLog systemLog=new SystemLog();
		systemLog.setMessage("Diagnosis created for "+diagnosis.getUser().getFirstName()+" "+diagnosis.getUser().getLastName()
				+ "by "+diagnosis.getDoctorName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		diagnosisRepository.save(diagnosis);
	}
	
	public List<User> getAllPatients(){
		List <User> users = userRepository.findAll().stream().filter(e->e.getRole().equals("PATIENT")).collect(Collectors.toList());
		return users;
	}
	
	public List<Diagnosis> getAllDiagnosis(User user){
		return diagnosisRepository.findByUser(user);
	}
	
	public Diagnosis findByDiagnosis(int diagnosisId) {
		return diagnosisRepository.findByDiagnosisID(diagnosisId);
	}

}
