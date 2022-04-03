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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "patient_query")
public class PatientQuery {
	
	 @Id
	    @Column(name = "queryId",nullable = false)
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long queryId;

	 @JoinColumn(name = "userId", nullable=false)
	    @NotNull
	    @ManyToOne
	    private User user;
	 
	 @Column(name = "querydescription")
	    private String querydescription;
	 
	 @Column(name = "queryresolution")
	    private String queryresolution;
	 
	 @Column(name = "querystatus")
	    private String querystatus;
	 
	 @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		@Temporal(TemporalType.TIMESTAMP)
		private Date querySubmissionTime;

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getQuerydescription() {
		return querydescription;
	}

	public void setQuerydescription(String querydescription) {
		this.querydescription = querydescription;
	}

	public String getQueryresolution() {
		return queryresolution;
	}

	public void setQueryresolution(String queryresolution) {
		this.queryresolution = queryresolution;
	}

	public String getQuerystatus() {
		return querystatus;
	}

	public void setQuerystatus(String querystatus) {
		this.querystatus = querystatus;
	}

	public Date getQuerySubmissionTime() {
		return querySubmissionTime;
	}

	public void setQuerySubmissionTime(Date querySubmissionTime) {
		this.querySubmissionTime = querySubmissionTime;
	}
}
