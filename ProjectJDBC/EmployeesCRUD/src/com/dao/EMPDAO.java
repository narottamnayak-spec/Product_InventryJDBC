package com.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


import com.model.Employee;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

public class EMPDAO {
//	Class.forName("com.mysql.cj.jdbc.Driver");
//    System.out.println("DoNE"); 
    
    private String URL="jdbc:mysql://localhost:3306/projects";
    private String ROOT="root";
    private String PASS="Narottam@123";
    
    
    private Connection getConnection() throws SQLException{
   return DriverManager.getConnection(URL,ROOT,PASS); 
    }
    
//  INSERTING......
   
    public void addEmployee(Employee emp) throws SQLException {
    	
    	 System.out.println("hsh");
    	String query="insert into karamchari(id,name, department,salary) values(?,?,?,?)";
    	Connection con=getConnection();
    	
    	PreparedStatement ps = con.prepareStatement(query);
    	int id=emp.getId();
    	String name=emp.getName();
    	String dept=emp.getDepartment();
    	double sal=emp.getSalary();
    	
    	ps.setInt(1, id);
    	ps.setString(2,name);
    	ps.setString(3,dept);
    	ps.setDouble(4, sal);
    	ps.executeUpdate();
    	
    }
    public List<Employee> getAllEmployees() throws SQLException{
    		List<Employee> list=new ArrayList<>();
    		String sql= "select *from karamchari";
    		Connection con =getConnection();
    		Statement st=con.createStatement();
    		ResultSet rs=st.executeQuery(sql);
    		while(rs.next()) {
    			int id=rs.getInt(1);
    			String name=rs.getString(2);
    			String department=rs.getString(3);
    			double salary= rs.getDouble(4);
    			Employee e=new Employee(id,name,department,salary);
    			list.add(e);
    			
    		}
    		return list;
    
    	}
//    	
//    	//update
//    
    public void updateEmployee(Employee emp) {
    	String sql="update karamchari set name=?,department=?,salary=? where id=?";
    	
    try
    	(Connection con=getConnection();
    		PreparedStatement ps = con.prepareStatement(sql)){
    	ps.setString(1,emp.getName());
    	ps.setString(2, emp.getDepartment());
    	ps.setDouble(3, emp.getSalary());
    	ps.setInt(4, emp.getId());
    	ps.executeUpdate();
    }
    catch(SQLException e) {
    	e.printStackTrace();
    }	
    
    }
}



