package com.asu.project.hospital.dao;

import java.io.Serializable;

public interface PatientDaoInterface<T, Id extends Serializable>{
	public void persist(T userEntity);

}
