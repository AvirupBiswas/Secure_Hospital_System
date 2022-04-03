package com.asu.project.hospital.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AppointmentRepository;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.repository.InsuranceClaimsRepository;
import com.asu.project.hospital.repository.InsuranceDetailsRepository;
import com.asu.project.hospital.repository.LabTestRepository;
import com.asu.project.hospital.repository.PatientPaymentRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.repository.UserRepository;

@Service
public class PatientService {
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	InsuranceDetailsRepository insuranceDetailsRepository;
	
	@Autowired
	InsuranceClaimsRepository insuranceClaimRepository;
	
	@Autowired
	PatientPaymentRepository patientPaymentRepository;
	
	@Autowired
	AppointmentRepository appointmentRepository;
	
	@Autowired
	LabTestRepository labTestRepository;
	
	@Autowired
	DiagnosisRepository diagnosisRepository;
	
	@Autowired
	SystemLogRepository systemLogRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	public void updatePatientInfo(Patient patient) {
		User user= userService.getLoggedUser();
		patient.setUser(user);
		patientRepository.save(patient);
	}
	
	public void addInsuranceDetails(InsuranceDetails insuranceDetails) {
		User user= userService.getLoggedUser();
		insuranceDetails.setUser(user);
		insuranceDetailsRepository.save(insuranceDetails);
	}
	
	public void editInsuranceDetails(InsuranceDetails insuranceDetails) {
		User user= userService.getLoggedUser();
		InsuranceDetails details=insuranceDetailsRepository.findByUser(user);
		details.setInsuranceId(insuranceDetails.getInsuranceId());
		details.setInsuranceName(insuranceDetails.getInsuranceName());
		details.setProvider(insuranceDetails.provider);
		insuranceDetailsRepository.save(details);
	}
	
	public void addInsuranceClaimRequest(InsuranceClaims claim) {
		User user= userService.getLoggedUser();
		InsuranceDetails details=insuranceDetailsRepository.findByUser(user);
		claim.setInsuranceDetails(details);
		claim.setUser(user);
		SystemLog systemLog=new SystemLog();
		systemLog.setMessage("Insurance claim raised by "+user.getFirstName()+" "+user.getLastName()
				+ ",for "+claim.getAmount());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		insuranceClaimRepository.save(claim);
	}
	
	public InsuranceDetails getInsuranceDetails(User user) {
		return insuranceDetailsRepository.findByUser(user);
	}
	
	public List<InsuranceClaims> findAllClaims(User user){
		return insuranceClaimRepository.findByUser(user);
	}
	
	public List<Appointment> findAllAppointments(User user){
		return appointmentRepository.findByUser(user);
	}
	
	public List<PatientPayment> findAllPaymentsByStatus(){
		User user=userService.getLoggedUser();
		List<PatientPayment> patientPayments=patientPaymentRepository.findByUser(user)
				.stream().filter(e -> e.getStatus().equals("Pending"))
				.collect(Collectors.toList());
		return  patientPayments;
	}
	
	public List<PatientPayment> findAllPaymentsPaid(){
		User user=userService.getLoggedUser();
		List<PatientPayment> patientPayments=patientPaymentRepository.findByUser(user)
				.stream().filter(e -> !e.getStatus().equals("Pending"))
				.collect(Collectors.toList());
		return  patientPayments;
	}
	
	public void createLabRequest(LabTest labTest) {
		User user=userService.getLoggedUser();
		labTest.setUser(user);
		labTest.setStatus("Requested");
		labTest.setPrice(new BigDecimal(100));
		SystemLog systemLog=new SystemLog();
		systemLog.setMessage("Lab Test "+labTest.getTestName()+" requested by "+user.getFirstName()+" "+user.getLastName()
				);
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		labTestRepository.save(labTest);
	}
	
	public List<LabTest> viewLabTests(User user){
		List<LabTest> labTests=labTestRepository.findByUser(user)
				.stream().filter(e->!e.getStatus().equals("Requested"))
				.collect(Collectors.toList());
		return labTests;
		
	}
	
	public List<Diagnosis> viewAllDiagnosis(User user){
		return diagnosisRepository.findByUser(user);
	}
	
	public void requestLabTest(int labTestId) {
		LabTest labtest=labTestRepository.getById(labTestId);
		System.out.println(labTestId);
		labtest.setStatus("Pending");
		labTestRepository.save(labtest);
	}
	
	public void makePayment(Long paymentID) {
		User user=userService.getLoggedUser();
		PatientPayment patientPayment=patientPaymentRepository.getById(paymentID);
		patientPayment.setPaymentType("card");
		patientPayment.setStatus("paid");
		SystemLog systemLog=new SystemLog();
		systemLog.setMessage("Payment made by"+user.getFirstName()+" "+user.getLastName()
				+ ",for "+patientPayment.getAmount()+" via"+patientPayment.getPaymentType());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		patientPaymentRepository.save(patientPayment);
	}
	
	public void makePaymentInsurance(Long paymentID) {
		User user=userService.getLoggedUser();
		PatientPayment patientPayment=patientPaymentRepository.getById(paymentID);
		patientPayment.setPaymentType("insurance");
		patientPayment.setStatus("Pending Insurance");
		InsuranceClaims claim=new InsuranceClaims();
		claim.setAmount(patientPayment.getAmount());
		claim.setPurpose(patientPayment.getPurpose());
		claim.setStatus("Pending");
		claim.setPatientPayment(patientPayment);
		addInsuranceClaimRequest(claim);
		SystemLog systemLog=new SystemLog();
		systemLog.setMessage("Payment made by"+user.getFirstName()+" "+user.getLastName()
				+ ",for "+patientPayment.getAmount()+" via"+patientPayment.getPaymentType());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		patientPaymentRepository.save(patientPayment);
	}
	
	public List<User> getDoctorsList(){
		List<User> doctors= userRepository.findAll().stream().filter(e->e.getRole().equals("DOCTOR"))
				.collect(Collectors.toList());
		return doctors;
	}

}
