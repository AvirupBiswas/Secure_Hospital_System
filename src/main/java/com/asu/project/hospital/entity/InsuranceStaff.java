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
	@JoinColumn(name="userID", nullable=false)
	private User user;
	
	@Column(name="contact")
	private Long contact;
	
	@Column(name="address")
	private String address;

	public InsuranceStaff(User user, Long contact, String address) {
		super();
		this.user = user;
		this.contact = contact;
		this.address = address;
	}

	public InsuranceStaff() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getPhoneNumber() {
		return contact;
	}

	public void setPhoneNumber(Long contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
