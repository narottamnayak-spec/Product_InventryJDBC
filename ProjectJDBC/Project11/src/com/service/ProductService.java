package com.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;

import com.dao.ProductDAO;
import com.model.Product;

public class ProductService {
	//product services 
	private ProductDAO pdao=new ProductDAO();
	
	public void insertProduct(Product product) throws SQLException {
		pdao.insertProduct(product);
}


	public List<Product> getAllProducts() throws SQLException{
		return pdao.getAllProducts();

	}
	public List<Product> getProductsById(int pid) throws SQLException{
		return pdao.getProductsById(pid);
	}
	public void updateProduct(Product product) throws SQLException {
		pdao.updateProduct(product);
	}

	public void deleteProduct(int id) throws SQLException {
		pdao.deleteProduct(id);
	}
	
//	purchase....
	
	public void purchaseProducts(List<Integer> productIds ,List<Integer> quantities) throws SQLException {
		pdao.purchaseProducts(productIds, quantities);
	}
		
	
	}
	

