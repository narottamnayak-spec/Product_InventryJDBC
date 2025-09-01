package com.dao;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.model.Category;
import com.model.Product;

public class CategoryDAO {
	private String URL = "jdbc:mysql://localhost:3306/projects";
	private String ROOT = "root";
	private String PASS = "Narottam@123";

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, ROOT, PASS);
	}

	// insert
	public void insertCategory(Category category) throws SQLException {
		String query = "Insert into category(cname) values(?)";
		try(Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(query)){
			
		String s = category.getName();
		ps.setString(1, s);
		
		ps.executeUpdate();
		System.out.println(" category inserted Successfully");
	}
		catch(SQLException e) {
			e.printStackTrace();
		}
			
		}

	//get Allcatogeroy
	public List<Category> getAllcategories()  {
		List<Category> list = new ArrayList<>();
		String query = "Select *from Category";
		try {
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(query);

		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			int id = rs.getInt(1);
			String name = rs.getString(2);
			Category c = new Category(id, name);
			list.add(c);
		}
	}
	
	catch(SQLException e) {
		e.printStackTrace();
	}
		return list;
		
	}

//getcategorybyId
		public List<Category> getCategoryById(int id) {
			List<Category> list = new ArrayList<>();
		
	        String query = "SELECT * FROM category WHERE cid=?";
	        try (
	    		Connection con = getConnection();
	    		PreparedStatement ps = con.prepareStatement(query)){
	    			ps.setInt(1, id);
	    		ResultSet rs = ps.executeQuery();
	    		while(rs.next()) {
	    			int sd = rs.getInt(1);
	    			String name = rs.getString(2);
	    			Category cd = new Category(sd, name);
	    			list.add(cd);
	    		}
	    		
	        }
	        catch(SQLException e) {
	        	e.printStackTrace();
	       
}
			return list;
}
		
		public void updateCategory(Category category) throws SQLException {
			String query="update category set cname=? where cid=?";
			
			try(Connection con= getConnection();
					PreparedStatement ps=con.prepareStatement(query)){
				ResultSet rs=ps.executeQuery();
				ps.setString(1,category.getName());
				ps.setInt(2, category.getId());
				ps.executeUpdate();
		
}
		}
		
		
		public void deleteCategory(int id) throws SQLException {
			String query="delete  from Category where cid=? ";
					Connection con=getConnection();
			PreparedStatement ps=con.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
}
}


