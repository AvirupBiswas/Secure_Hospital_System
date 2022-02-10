package com.asu.project.hospital.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="patient")
public class PatientInformation {
	@Id
	@Column(name="patientId", nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long patientId;
	@OneToOne
	@MapsId
	@JoinColumn(name = "email", nullable=false)
	private User user;
	@NotNull(message = "Required*")
	private int age;
	@NotNull(message = "Required*")
	private float height;
	@NotNull(message = "Required*")
	private float weight;
	@NotNull(message = "Required*")
	private String address;
	@NotNull(message = "Required*")
	private double phoneNumber;
	@NotNull(message = "Required*")
	private String gender;
	
	
	public PatientInformation(@JsonProperty("user") User user,@JsonProperty("age")  int age,
			@JsonProperty("height") float height, @JsonProperty("weight") float weight,
			@JsonProperty("address") String address, @JsonProperty("phoneNumber") double phoneNumber,
			@JsonProperty("gender") String gender) {
		this.user = user;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
	}
	

	public PatientInformation() {
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	public float getWeight() {
		return weight;
	}


	public void setWeight(float weight) {
		this.weight = weight;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public double getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(double phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	
	
	

	
	

}
