package com.asu.project.hospital.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="insurance")
public class InsuranceDetails {
	
	
	@Column(name="Id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long Id;
	
	@Column(name="insuranceId")
	@NotNull
	public String insuranceId;
	
	@Column(name="insuranceName")
	public String insuranceName;
	
	@Column(name="provider")
	public String provider;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="userId", nullable=false)
	public User user;
	
	@OneToMany(mappedBy = "insuranceDetails")
	public Set<InsuranceClaims> claims;


	public InsuranceDetails(@NotNull Long id, @NotNull String insuranceId, String insuranceName, String provider,
			User user, Set<InsuranceClaims> claims) {
		Id = id;
		this.insuranceId = insuranceId;
		this.insuranceName = insuranceName;
		this.provider = provider;
		this.user = user;
		this.claims = claims;
	}


	public InsuranceDetails() {
	}

	public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


	public String getInsuranceId() {
		return insuranceId;
	}

	public Set<InsuranceClaims> getClaims() {
		return claims;
	}


	public void setClaims(Set<InsuranceClaims> claims) {
		this.claims = claims;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
