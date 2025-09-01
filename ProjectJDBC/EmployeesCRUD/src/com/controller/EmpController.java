package com.controller;

import com.model.Employee;


import com.service.EmpService;

import java.sql.SQLException;
import java.util.*;

public class EmpController {
	public static void main(String args[]) throws SQLException {
		
	Scanner sc= new Scanner(System.in);
		EmpService service=new EmpService();
		
		while(true) {
			System.out.println("----Employee Management----");
			System.out.println("1.add employee");
			System.out.println("2.retrive Employee ----");
			System.out.println("3. update by id");
			System.out.println("4. delete ----");
			System.out.println("5.exit");
			System.out.println("enter your choice");
			int choice=Integer.parseInt(sc.nextLine());
			
			 switch (choice) {
             case 1:
            	             		  
            	         System.out.print("Enter id: ");
            	         int id = Integer.parseInt(sc.nextLine());
            	         System.out.print("Enter name: ");
            	         String name = sc.nextLine();
            	         System.out.print("Enter dept: ");
            	         String dept = sc.nextLine();
            	       
            	         System.out.print("Enter Salary: ");
            	         double salary = Double.parseDouble(sc.nextLine());
            	         
            	         Employee obj=new Employee(id,name,dept,salary);
            	         service.addEmployee(obj);
            	         break;
            	         
            	         case 2:
            	        	 List<Employee> list=service.getAllEmployees();
            	         for(Employee e:list) {
            	        	 System.out.println(e);
            	         }
            	         break;
//            	         
            	         case 3:
            	        	 System.out.print("Enter Empoyee Idto Update: ");
            	        	   int uid = Integer.parseInt(sc.nextLine());
            	        	   System.out.print("Enter  NEW name: ");
                  	         String nname = sc.nextLine();
                  	         System.out.print("EnterNEW  dept: ");
                  	         String ndept = sc.nextLine();
                  	       
                  	         System.out.print("Enter  new Salary: ");
                  	         double nsal = sc.nextDouble();
                  	         
                  	         Employee e =new Employee(uid,nname,ndept,nsal);
                  	         service.updateEmployee(e);
                  	         
                  	       System.out.print("EMployee udated: ");
            	        	   break;
		}
		}
	}
	}


	
