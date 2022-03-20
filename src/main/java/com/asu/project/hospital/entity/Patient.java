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
@Table(name="patient")
public class Patient {
	
	@Id
    @Column(name = "patientID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long patientID;
	
	@OneToOne
	@JoinColumn(name="userId", nullable=false)
	private User user;
	
	@Column(name="height")
	private double height;
	
	@Column(name="phonenumber")
	private long phoneNumber;
	
	

	@Column(name="weight")
	private double weight;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="age")
	private int age;
	
	@Column(name="address")
	private String Address;

	public Patient(User user, double height, double weight, String gender, int age, String address) {
		super();
		this.user = user;
		this.height = height;
		this.weight = weight;
		this.gender = gender;
		this.age = age;
		Address = address;
	}

	public Patient() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
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
