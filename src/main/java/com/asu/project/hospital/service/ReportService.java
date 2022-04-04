package com.asu.project.hospital.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {


	public byte[] exportReport(List<?> values,String jrxmlFile) throws FileNotFoundException, JRException {
		// load file and compile it
		/*
		 * File file = ResourceUtils.getFile("classpath:"+jrxmlFile); JasperReport
		 * jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		 */
		InputStream is = ReportService.class.getResourceAsStream("/"+jrxmlFile);
		JasperReport jasperReport = JasperCompileManager.compileReport(is);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(values);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "Avirup");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		byte data[] = JasperExportManager.exportReportToPdf(jasperPrint);
		return data;
	}

}
