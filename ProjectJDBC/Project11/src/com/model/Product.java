package com.model;

public class Product {
	private int id;
	private String name;
	private double price;
	private int quantity;
	private int categoryId;
	
	Product(){}
	
	public Product(int id, String name, double price, int quantity, int categoryId) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categoryId = categoryId;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "Product Id=" + id + 
				", name=" + name + 
				", price=" + price + 
				", quantity=" + quantity + 
				", categoryId="+ categoryId ;
	}
	
	
}
