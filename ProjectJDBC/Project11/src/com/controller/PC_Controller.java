package com.controller;


import java.sql.SQLException;


import java.util.*;
import com.service.*;
import com.model.*;



public class PC_Controller {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
	Scanner sc= new Scanner(System.in);
	

	CategoryService  cs= new CategoryService();
	ProductService service=new ProductService();
	
	
		// INSERTION
	
	while(true) {
		System.out.println("----pc Management----");
		System.out.println("1.add categ");
		System.out.println("2.add product ----");
		
		System.out.println("3. GetALlCAteGories");
		System.out.println("4. GetAllProducts");
		
		System.out.println("5. GetALlCAteGories Byid");
		System.out.println("6. GetAllProductsById");
		System.out.println("7. Update product");
		System.out.println("8. update category");
		System.out.println("9. delete  product");
		System.out.println("10. delete category");
//		purchase
		System.out.println("11.🔍Available Products in Store");
		System.out.println("12.purchase products");
		
		
		System.out.println(".exit");
		System.out.println("enter your choice");
		int choice=Integer.parseInt(sc.nextLine());
		
		 switch (choice) {
         case 1:
        
	         System.out.print("Enter CategoryName: ");
	         String name = sc.nextLine();
	       
	         
	         Category obj=new Category(0,name);
	         cs.insertCategory(obj);
	         System.out.println("Category added");
	         break;
        	
         case 2:
        	 System.out.print("products............... ");
        
	         System.out.print("Enter name: ");
	         String pname = sc.nextLine();
	         System.out.print("Enter price: ");
	       double price = Double.parseDouble(sc.nextLine());
	         System.out.print("Enter quantity: ");

	       int quantity=Integer.parseInt(sc.nextLine());
	       
	         System.out.print("Enter cid: ");
	         int cid = Integer.parseInt(sc.nextLine());
	         
	         Product obj1=new Product(0,pname,price,quantity,cid);
	         service.insertProduct(obj1);
	         
	         System.out.println("product added");
	         break;
         case 3:
        	 List<Category>list =cs.getAllcategories();
        	 for(Category c:list) {
        		 System.out.println(c);
        	 }
        	 break;
         case 4:
        	 List<Product>list1 =service.getAllProducts();
        	 for(Product d:list1) {
        		 System.out.println(d);
        	 }
        		 break;
        case 5:
        	System.out.println("enterId that you want all category by id");
        	 int id = Integer.parseInt(sc.nextLine());
			List<Category>list2 =cs.getCategoryById(id);
        	 for(Category c1:list2) {
        		 System.out.println(c1);
        	 }
        		 break;
        case 6:
        	System.out.println("enterId that you want all Product by id");
       	 int pid = Integer.parseInt(sc.nextLine());
        	 List<Product> list3=service.getProductsById(pid);
        	 for(Product p:list3) {
        		 System.out.println(p);
        	 }
        		 break;
         case 7:
        	 System.out.print("Updation............ ");
             
        	 System.out.print("Enter product id to update: ");
	         int proId = Integer.parseInt(sc.nextLine());
	         
	         System.out.print("Enter new name: ");
	         String nname = sc.nextLine();
	         
	         System.out.print("Enter new  price: ");
	       double nprice = Double.parseDouble(sc.nextLine());
	       
	         System.out.print("Enter new quantity: ");

	       int nquant=Integer.parseInt(sc.nextLine());
	       
	         System.out.print("Enter new cid: ");
	         int ncid = Integer.parseInt(sc.nextLine());
	         
	         Product upPro=new Product(proId,nname,nprice,nquant,ncid);
	         service.updateProduct(upPro);
	         
	         System.out.println("product Updatedd..");
	         break;
         case 8:
        	 System.out.print("Enter category id to update: ");
	         int upCid = Integer.parseInt(sc.nextLine());
	         
	         System.out.print("Enter new cname: ");
	         String ncname = sc.nextLine();
	         
	         Category uC=new Category(upCid,ncname);
	         cs.updateCategory(uC);
	         System.out.print("Update category ");
	         break;
         case 9:
        	 System.out.print("Enterprduct id to delete: ");
	         int pidTdel = Integer.parseInt(sc.nextLine());
	         service.deleteProduct(pidTdel);
	         
        	 
        	 
        	 
        	 break;
         case 10:
        	 System.out.print("Enter category id to delete: ");
	         int cidTdel = Integer.parseInt(sc.nextLine());
	         cs.deleteCategory(cidTdel);
	         
        	 
        	 break;
        	 
//        	 purchase
         case 11:
        	    System.out.println("🔍 Available Products in Store:");
        	    List<Product> productList2 = service.getAllProducts();
        	    
        	    if (productList2.isEmpty()) {
        	        System.out.println("⚠ No products available in the store.");
        	    } else {
        	        for (Product p : productList2) {
        	            System.out.println(p);
        	        }
        	    }
        	    break;
         case 12 :
        	 
        	 List<Integer> productIds=new ArrayList<>();
        	 List<Integer> quantities=new ArrayList<>();
        	 List<Double> totalPriceList=new ArrayList<>();
        	 
        	 double grandTotal=0;
        	 
        	 while(true){
        		 
        		 System.out.println("First you hove to check All products Details in CAse 11 : ");
        		 System.out.print("Enter product id to Purchase: ");
    	         int pId = Integer.parseInt(sc.nextLine());
    	           	         
    	         System.out.print("Enter quantity: ");
    	         int qty = Integer.parseInt(sc.nextLine());
    	         
    	         // get product to know price
    	         
    	         List<Product> productList=service.getProductsById(pId);
    	         if(productList.isEmpty()) {
    	        	 System.out.print("product not found : ");
    	        	 continue;
    	        	 
    	         }
    	         Product product=productList.get(0);
    	         if(qty>product.getQuantity()) {
    	        	 System.out.print("ONLY "+ product.getQuantity()+"items availabe..TRY AGAIN");
    	        	 continue;
    	        	 
    	         }
    	         productIds.add(pId);
    	         quantities.add(qty);
    	         double total=product.getPrice()*qty;
    	         totalPriceList.add(total);
    	         
    	         grandTotal+=total;
    	         
    	         System.out.print(" "+product.getName()+"added|Qty"+qty+ "| Total Rs: "+total);
    	         System.out.print("product not found : ");
    	       
    	         System.out.print("Do you want to but more?(Y/N) : ");
    	         
    	         String more=sc.nextLine();
    	         if(!more.equalsIgnoreCase("y")) 
    	        	 break;
    	         }
    	         //final
    	         
    	         
    	         System.out.println("--------------------------------------------------");
    	         System.out.println("🧾 Final Bill:");
    	         for (int i = 0; i < productIds.size(); i++) {
    	             Product product2 = service.getProductsById(productIds.get(i)).get(0);
    	             System.out.println(product2.getName() + " | Qty: " + quantities.get(i) + " | ₹" + totalPriceList.get(i));
    	         }
    	         System.out.println("--------------------------------------------------");
    	         System.out.println("💰 Grand Total: ₹" + grandTotal);
    	         System.out.print("Proceed to purchase? (yes/no): ");
    	         String confirm = sc.nextLine();

    	         if (confirm.equalsIgnoreCase("yes")) {
    	             try {
    	                 service.purchaseProducts(productIds, quantities);
    	                 System.out.println("🎉 Purchase successful!");
    	             } catch (SQLException e) {
    	                 System.out.println("❌ Error during purchase: " + e.getMessage());
    	             }
    	         } else {
    	             System.out.println("🛑 Purchase cancelled.");
    	         }
    	         break;
        	 }
        	 
        	 break;
        		 
        
	
	}
	}
	
	}



//String password="Nayak123";
//boolean loop=true;
//while(loop) {
//	System.out.println("----WELL COME TO PRODUCT STORE----");
//	System.out.println("1.ONLY ADMIN  ");
//	System.out.println("2.USERS PANEL");
//	System.out.println("3.exit");
//	System.out.println("enter your PANEL NO.");
//	int pn=Integer.parseInt(sc.nextLine());
//	
//	 switch (pn) {
//     case 1:
//    	
//    	 break;
//     case 2:
//     
//     break;
//     case 3:
//     loop = false;
//     default:
//    	 System.out.println("Invalid Pannel no.");
//	 }
//}
// System.out.println("thank you for the visit");
//
// private static void adminWorks(Scanner sc, ProductService service) {
//        System.out.print("Enter admin password: ");
//        String ap = sc.nextLine();
//        if (!password.equals(ap)) {
//            System.out.println("Incorrect password.");1
//            return;
//









