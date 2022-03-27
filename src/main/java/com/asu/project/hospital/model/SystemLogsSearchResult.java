package com.asu.project.hospital.model;

import java.util.List;

import com.asu.project.hospital.entity.SignInHistory;
import com.asu.project.hospital.entity.SystemLog;

public class SystemLogsSearchResult {

	private List<SystemLog> systemLogsList;

	public List<SystemLog> getSystemLogsList() {
		return systemLogsList;
	}

	public void setSystemLogsList(List<SystemLog> systemLogsList) {
		this.systemLogsList = systemLogsList;
	}
}
