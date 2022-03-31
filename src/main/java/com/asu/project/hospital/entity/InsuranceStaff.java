package com.asu.project.hospital.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="insurance_staff")
public class InsuranceStaff {
	@Id
    @Column(name = "insuranceStaffID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long insuranceStaffID;
	
	@OneToOne
	@JoinColumn(name="userId", nullable=false)
	private User user;
	
	@Column(name="phoneNumber")
	private Long phoneNumber;
	
	@Column(name="address")
	private String address;

	public Long getInsuranceStaffID() {
		return insuranceStaffID;
	}

	public void setInsuranceStaffID(Long insuranceStaffID) {
		this.insuranceStaffID = insuranceStaffID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public InsuranceStaff(Long insuranceStaffID, User user, Long phoneNumber, String address) {
		super();
		this.insuranceStaffID = insuranceStaffID;
		this.user = user;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public InsuranceStaff() {
		super();
		// TODO Auto-generated constructor stub
	}

}