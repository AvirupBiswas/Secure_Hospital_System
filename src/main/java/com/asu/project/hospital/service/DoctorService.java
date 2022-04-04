package com.asu.project.hospital.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AppointmentRepository;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.repository.DoctorRepository;
import com.asu.project.hospital.repository.LabTestReportRepository;
import com.asu.project.hospital.repository.LabTestRepository;
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
	AppointmentRepository appointmentRepository;

	@Autowired
	SystemLogRepository systemLogRepository;

	@Autowired
	LabTestRepository labTestRepository;
	
	@Autowired
	LabTestReportRepository reportRepository;

	public void updateDoctorInfo(Doctor doctor) {
		User user = userService.getLoggedUser();
		doctor.setUser(user);
		doctorRepository.save(doctor);
	}

	public Diagnosis createDiagnosis(Diagnosis diagnosis) {
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Diagnosis created for " + diagnosis.getUser().getFirstName() + " "
				+ diagnosis.getUser().getLastName() + "by " + diagnosis.getDoctorName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return diagnosisRepository.save(diagnosis);
	}

	public void deleteDiagnosis(Diagnosis diagnosis) {
		List<LabTest> labTests=labTestRepository.findByDiagnosis(diagnosis);
		if(labTests!=null) {
			for(LabTest labtest:labTests) {
			LabTestReport report=reportRepository.findByLabTest(labtest);
			if(report!=null) {
				reportRepository.delete(report);
			}
			labTestRepository.delete(labtest);
			}
		}
		diagnosisRepository.delete(diagnosis);
	}

	public List<Appointment> getAllPatients() {
		/*
		 * List <User> patients =
		 * userRepository.findAll().stream().filter(e->e.getRole().equals("PATIENT")).
		 * collect(Collectors.toList()); List<Appointment> allAppointments =
		 * appointmentRepository.findAll(); List<Appointment> appointments =
		 * allAppointments.stream().filter(a->a.getDoctorEmail()==null &&
		 * a.getStatus().equals("Approved")).collect(Collectors.toList()); List<User>
		 * patientwithAppointmentList =
		 * appointments.stream().map(e->e.getUser()).collect(Collectors.toList()); List
		 * <User> patientwithAppointment =
		 * patients.stream().filter(e->patientwithAppointmentList.contains(e)).collect(
		 * Collectors.toList());
		 */

		List<Appointment> allAppointments = appointmentRepository.findAll();
		List<Appointment> patientwithAppointment = allAppointments.stream()
				.filter(a -> a.getDoctorEmail() == null && a.getStatus().equals("Approved"))
				.collect(Collectors.toList());

		return patientwithAppointment;
	}

	public List<Appointment> getAllSpecialAppointment(String doctorEmail) {
		/*
		 * List <User> patients =
		 * userRepository.findAll().stream().filter(e->e.getRole().equals("PATIENT")).
		 * collect(Collectors.toList()); List<User> patientwithAppointmentList =
		 * appointments.stream().map(e->e.getUser()).collect(Collectors.toList()); List
		 * <User> patientwithAppointment =
		 * patients.stream().filter(e->patientwithAppointmentList.contains(e)).collect(
		 * Collectors.toList());
		 */

		List<Appointment> allAppointments = appointmentRepository.findAll();
		List<Appointment> patientwithAppointment = allAppointments.stream()
				.filter(a -> (a.getDoctorEmail() != null && a.getDoctorEmail().equals(doctorEmail))
						&& a.getStatus().equals("Approved"))
				.collect(Collectors.toList());

		return patientwithAppointment;
	}

	public List<Diagnosis> getAllDiagnosis(User user) {
		return diagnosisRepository.findByUser(user);
	}

	public Diagnosis findByDiagnosis(int diagnosisId) {
		return diagnosisRepository.findByDiagnosisID(diagnosisId);
	}

	public List<LabTest> viewLabTests(User user) {
		List<LabTest> labTests = labTestRepository.findByUser(user).stream()
				.filter(e -> e.getStatus().equals("Reported")).collect(Collectors.toList());
		return labTests;

	}

}
