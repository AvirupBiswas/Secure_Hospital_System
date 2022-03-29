package com.asu.project.hospital.service;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.repository.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.LabStaffRepository;
import com.asu.project.hospital.repository.LabTestReportRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LabStaffService {
	@Autowired
	LabStaffRepository labStaffRepository;

	@Autowired
	LabTestRepository labTestRepository;

	@Autowired
	UserService userService;

	@Autowired
	LabTestReportRepository labTestReportRepository;

	public void updateLabStaffInfo(LabStaff labStaff) {
		User user = userService.getLoggedUser();
		labStaff.setUser(user);
		labStaffRepository.save(labStaff);
	}

	public List<LabTest> getLabTestsByStatus(String status) {
		return labTestRepository.findByStatus(status);
	}

	public User updateLabTestStatus(String status, Integer labTestId) {
		Optional<LabTest> labTestObj = labTestRepository.findById(labTestId);
		if (labTestObj.isPresent()) {
			labTestObj.get().setStatus(status);
			labTestRepository.save(labTestObj.get());
			return labTestObj.get().getUser();
		}
		return null;
	}

	public LabTest getLabTest(Integer labTestId) {
		Optional<LabTest> labTestObj = labTestRepository.findById(labTestId);
		if (labTestObj.isPresent()) {
			return labTestObj.get();
		}
		return null;
	}

	public void createLabTestReport(LabTestReport labTestReport, LabTest labtest) {
		labtest.setStatus("Reported");
		labTestRepository.save(labtest);
		labTestReport.setLabTest(labtest);
		labTestReportRepository.save(labTestReport);
	}

	public void deleteLabTestReport(Integer labTestReportId) {
		Optional<LabTestReport> labTestReport = labTestReportRepository.findById(labTestReportId);
		if (labTestReport.isPresent()) {
			LabTest labtest = labTestReport.get().getLabTest();
			labtest.setStatus("Archived");
			labTestRepository.save(labtest);
			labTestReportRepository.delete(labTestReport.get());
		}

	}

	public void UpdateLabTestReport(LabTestReport labTestReport, Integer labTestReportId) {
		Optional<LabTestReport> labTestReportObj = labTestReportRepository.findById(labTestReportId);
		if (labTestReportObj.isPresent()) {
			LabTestReport labTestReportObjVal = labTestReportObj.get();
			labTestReportObjVal.setTestResult(labTestReport.getTestResult());
			labTestReportRepository.save(labTestReportObjVal);
		}

	}

	public List<LabTestReport> getAllLabTestReports() {
		return labTestReportRepository.findAll();
	}

	public LabTestReport getLabTestReport(int parseInt) {
		Optional<LabTestReport> optionalLabTestReport = labTestReportRepository.findById(parseInt);
		return optionalLabTestReport.isPresent()?optionalLabTestReport.get():null;
	}
	
	public int getLabTestReportId(int labTestId) {
		LabTest labTest = getLabTest(labTestId);
		return labTestReportRepository.findByLabTest(labTest).getLabTestReportId();
	}
}
