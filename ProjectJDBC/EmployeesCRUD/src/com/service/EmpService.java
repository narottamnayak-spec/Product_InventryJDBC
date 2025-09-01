package com.service;

import java.sql.SQLException;

import java.util.List;
import com.dao.EMPDAO;
import com.model.Employee;

public class EmpService {
	
private EMPDAO dao=new EMPDAO();

public void addEmployee(Employee emp) throws SQLException {
	dao.addEmployee(emp);
	
}

public List<Employee> getAllEmployees() throws SQLException{
	return dao.getAllEmployees();

}
public void updateEmployee(Employee emp)throws SQLException {
	 dao.updateEmployee(emp);
}

}
