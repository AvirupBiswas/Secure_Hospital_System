package com.asu.project.hospital.model;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class LoginSingleTon {

	private Date timestamp;
	/*
	 * private static volatile LoginSingleTon INSTANCE = new LoginSingleTon();
	 * 
	 * 
	 * A private Constructor prevents any other class from instantiating.
	 * 
	 * private LoginSingleTon() { }
	 * 
	 * Static 'instance' method public static LoginSingleTon getInstance() { if
	 * (INSTANCE == null) { synchronized (LoginSingleTon.class) { if (INSTANCE ==
	 * null) { INSTANCE = new LoginSingleTon(); } } } return INSTANCE; }
	 */

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
