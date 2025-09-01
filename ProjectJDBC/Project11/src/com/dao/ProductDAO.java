package com.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.model.Product;


public class ProductDAO {
	private String URL="jdbc:mysql://localhost:3306/projects";
    private String ROOT="root";
    private String PASS="Narottam@123";
    
    
    private Connection getConnection() throws SQLException{
   return DriverManager.getConnection(URL,ROOT,PASS); 
    }
    public void insertProduct(Product product)throws SQLException {
    	String query="Insert into Product(pname, price, quantity,cid) values(?,?,?,?)";
    	Connection con=getConnection();
    	PreparedStatement ps = con.prepareStatement(query);
    
    
	String name=product.getName();
	double price=product.getPrice();
	int quantity=product.getQuantity();
	int cat_id=product.getCategoryId();
	
 	ps.setString(1, name);
	ps.setDouble(2,price);
	ps.setInt(3,quantity);
	ps.setInt(4, cat_id);
	ps.executeUpdate();
	System.out.println("product insertd..");

}
    
    //retreive
    
	public List<Product> getAllProducts() throws SQLException{
		List<Product> list=new ArrayList<>();
		String query="Select *from Product";
		Connection con=getConnection();
				PreparedStatement ps=con.prepareStatement(query);
				
				ResultSet rs= ps.executeQuery();
				while(rs.next()) {
					int id=rs.getInt(1);
					String name=rs.getString(2);
					double price=rs.getDouble(3);
					int quant=rs.getInt(4);
					int c=rs.getInt(5);
					
					Product p=new Product(id,name,price,quant,c);
					list.add(p);
				}
				
				return list;
}
//	getProductsById
	public List<Product> getProductsById(int id) throws SQLException{
		List<Product> list=new ArrayList<>();
		String query="Select  * from Product where pid=?" ;
		
		Connection con=getConnection();
				PreparedStatement ps=con.prepareStatement(query);
				ps.setInt(1,id);
				
				ResultSet rs= ps.executeQuery();
				while(rs.next()) {
					int d=rs.getInt(1);
					String name=rs.getString(2);
					double price=rs.getDouble(3);
					int quant=rs.getInt(4);
					int c=rs.getInt(5);
					
					Product p=new Product(d,name,price,quant,c);
					list.add(p);
				}
				
				return list;
}
	
//	update
	
	public void updateProduct(Product product) throws SQLException {
		String query="update product set pname=?,price=?, quantity=? cid=? where pid=?";
		
		try(Connection con= getConnection();
				PreparedStatement ps=con.prepareStatement(query)){
			
			ResultSet rs=ps.executeQuery();
			
			ps.setString(1, product.getName());
			ps.setDouble(2, product.getPrice());
			ps.setInt(3, product.getQuantity());
			ps.setInt(4, product.getCategoryId());
			ps.setInt(5, product.getId());
		}
	}
		
//		delete
		
		public void deleteProduct(int id) throws SQLException {
			String query="delete  from Product where pid=? ";
					Connection con=getConnection();
			PreparedStatement ps=con.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
		}
		
//		purchase products by user 
		
		
		public void purchaseProducts(List<Integer> productIds ,List<Integer> quantities) throws SQLException {
			Connection con=getConnection();
			con.setAutoCommit(false);
			
			try{for(int i=0; i<productIds.size();i++) {
				int pid=productIds.get(i);
				int quant=quantities.get(i);
				//check stock
				
				String query="select quantity,price,pname from product where pid=?";
				
				PreparedStatement ps=con.prepareStatement(query);
				ps.setInt(1, pid);
				ResultSet rs=ps.executeQuery();
				
				if(rs.next()) {
					int available=rs.getInt("quantity");
					String pname=rs.getString("pname");
					
					if(quant>available) {
						con.rollback();
						throw new SQLException("insufficient stock for product:" +pname);
					}
					
//					deduct stock
					
					String update="update product set quantity=quantity-? where pid=?";
					PreparedStatement pst=con.prepareStatement(update);
					pst.setInt(1, quant);
					pst.setInt(2, pid);
					pst.executeUpdate();
					
				}
				else {
					con.rollback();
					throw new SQLException("iproduct id not found:" +pid);
				}
				
			}
			con.commit();
		}
		catch (SQLException e) {
			con.rollback();
			throw e;
		}
			finally {
				con.setAutoCommit(true);
				con.close();
		
}
		}
}









