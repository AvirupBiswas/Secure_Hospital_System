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
@Table(name="doctor")
public class Doctor {
	
	@Id
    @Column(name = "doctorID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long doctorID;
	
	@OneToOne
	@JoinColumn(name="userId", nullable=false)
	private User user;
	
	@Column(name="phonenumber")
	private long phoneNumber;

	@Column(name="gender")
	private String gender;
	
	@Column(name="age")
	private int age;
	
	@Column(name="address")
	private String Address;

	public Doctor(User user, String gender, int age, String address) {
		super();
		this.user = user;
		this.gender = gender;
		this.age = age;
		Address = address;
		
	}
	
	public Doctor() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
	
}
