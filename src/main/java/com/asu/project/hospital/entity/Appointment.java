package com.asu.project.hospital.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "appointment")
public class Appointment {
	//Appointment ID
    @Id
    @Column(name = "appId",nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long appId;

    //userID (Foreign Key)
    @JoinColumn(name = "userId", nullable=false)
    @NotNull
    @ManyToOne
    private User user;
    
    @Column(name="type")
    private String type; 

  //Contact way
    @Column(name = "phoneNumber")
    private Long phoneNumber;
    
    //description
    @Column(name = "description")
    private String description;

    // Start time
    @Column(name = "startTime")
    @NotNull
    private Date startTime;
    
    @Column(name = "endTime")
    private Date endTime;
    
    @Column(name="status")
    private String status;
    
    @Column(name="doctorEmail")
    private String doctorEmail;
    
    

    public Appointment() {
	}

    
 

	public Appointment(Long appId, @NotNull User user, String type, String description,
			@NotNull Date startTime, Date endTime, String status,Long phoneNumber) {
		this.appId = appId;
		this.user = user;
		this.type = type;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.phoneNumber = phoneNumber;
	}




	public String getDoctorEmail() {
		return doctorEmail;
	}




	public void setDoctorEmail(String doctorEmail) {
		this.doctorEmail = doctorEmail;
	}




	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	

}
