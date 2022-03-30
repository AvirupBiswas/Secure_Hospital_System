package com.asu.project.hospital.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.asu.project.hospital.entity.User;

public class Insurance {
	public String insuranceId;
	
	public String insuranceName;
	
	public String provider;
	
	public String email;

	public String getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(String insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private Insurance(String insuranceId, String insuranceName, String provider, String email) {
		super();
		this.insuranceId = insuranceId;
		this.insuranceName = insuranceName;
		this.provider = provider;
		this.email = email;
	}

	private Insurance() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
