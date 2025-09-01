package com.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;

import com.model.Category;

import com.dao.*;



public class CategoryService {

	
	private CategoryDAO cdao=new CategoryDAO();
	
	
	
	public void insertCategory(Category category) throws SQLException {
		cdao.insertCategory(category);

}
	public List<Category> getAllcategories() throws SQLException{
		return cdao.getAllcategories();
	
	
		
	}
	
	public List<Category> getCategoryById(int id) {
		return cdao.getCategoryById(id);
	}

public void updateCategory(Category category) throws SQLException {
	cdao.updateCategory(category);
}

public void deleteCategory(int id) throws SQLException {
	cdao.deleteCategory(id);
}
}
