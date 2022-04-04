package com.asu.project.hospital.model;

import java.time.LocalDateTime;
import java.util.Date;

public class BlockChainDiagnosisObject {
	
	String id;
	
	String patient_name;
	
	String content;
	
	Date date;

	public BlockChainDiagnosisObject(String id, String patient_name, String content, Date date) {
		this.id = id;
		this.patient_name = patient_name;
		this.content = content;
		this.date = date;
	}

	
	public BlockChainDiagnosisObject() {
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatient_name() {
		return patient_name;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
