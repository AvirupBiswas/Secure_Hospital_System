package com.asu.project.hospital.entity;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name="LabTestReport")
public class LabTestReport {

    @Column(name="testReportId", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labTestReportId;

    @Column(name="testName")
    private String testName;

    @Column(name="testResult")
    private String testResult;

    @OneToOne
    @JoinColumn(name="testId",nullable = false)
    private LabTest labTest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    public int getLabTestReportId() {
        return labTestReportId;
    }

    public void setLabTestReportId(int labTestReportId) {
        this.labTestReportId = labTestReportId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public LabTest getLabTest() {
        return labTest;
    }

    public void setLabTest(LabTest labTest) {
        this.labTest = labTest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LabTestReport(int labTestReportId, String testName, String testResult, LabTest labTest, User user) {
        this.labTestReportId = labTestReportId;
        this.testName = testName;
        this.testResult = testResult;
        this.labTest = labTest;
        this.user = user;
    }

    public LabTestReport() {
    }
}
