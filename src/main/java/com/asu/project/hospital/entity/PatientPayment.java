package com.asu.project.hospital.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="patient_payment")
public class PatientPayment {
	
	@Id
    @Column(name = "paymentID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long paymentID;
	
	@ManyToOne
	@JoinColumn(name="userId", nullable=false)
	private User user;

	@Column(name="status")
	private String status;
	
	@Column(name="amount")
	private BigDecimal amount;

	public PatientPayment(User user, BigDecimal amount, String status) {
		super();
		this.user = user;
		this.status = status;
		this.amount = amount;
	}

	public PatientPayment() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	

}
