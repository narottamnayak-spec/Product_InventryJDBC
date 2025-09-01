package com.gui;

	import com.model.Category;
	import com.model.Product;
	import com.service.CategoryService;
	import com.service.ProductService;

	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.util.ArrayList;
	import java.util.List;
	import java.awt.event.*;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.sql.SQLException;
	import java.time.LocalDateTime;
	import java.time.format.DateTimeFormatter;
	import java.util.*;

	/**
	 * MainGUI - Swing front-end that uses your existing backend (CategoryService, ProductService) as-is.
	 * - Place file as com.gui.MainGUI
	 * - Keep your existing com.model, com.dao, com.service classes unchanged.
	 *
	 * Features:
	 * - Category tab: add / update / delete / refresh (JTable)
	 * - Product tab: add / update / delete / refresh (JTable + category combo)
	 * - Purchase tab: select products -> add to cart -> Generate Bill (random ID + date/time)
	 * - Save Bill to text file
	 */
	public class MainGUI extends JFrame {

	    private final CategoryService categoryService = new CategoryService();
	    private final ProductService productService = new ProductService();

	    // Category components
	    private DefaultTableModel catModel;
	    private JTable catTable;
	    private JTextField txtCatId, txtCatName;

	    // Product components
	    private DefaultTableModel prodModel;
	    private JTable prodTable;
	    private JTextField txtProdId, txtProdName, txtProdPrice, txtProdQty;
	    private JComboBox<Category> comboCats;

	    // Purchase components
	    private DefaultTableModel shopModel;
	    private JTable shopTable;
	    private DefaultTableModel cartModel;
	    private JTable cartTable;
	    private JTextField txtBuyQty;
	    private JLabel lblBillId, lblDateTime, lblGrand;
	    private JTextArea txtReceipt;

	    private final List<Integer> cartProductIds = new ArrayList<>();
	    private final List<Integer> cartQuantities = new ArrayList<>();

	    public MainGUI() {
	        setTitle("Shop Management System");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(1100, 700);
	        setLocationRelativeTo(null);

	        JTabbedPane tabs = new JTabbedPane();
	        tabs.addTab("Category", buildCategoryPanel());
	        tabs.addTab("Product", buildProductPanel());
	        tabs.addTab("Purchase", buildPurchasePanel());

	        add(tabs);

	        // initial load
	        refreshCategoryTable();
	        refreshProductTable();
	        refreshCategoryCombo();
	        refreshShopProducts();
	    }

	    // ---------------- Category Tab ----------------
	    private JPanel buildCategoryPanel() {
	        JPanel panel = new JPanel(new BorderLayout(6,6));

	        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        form.add(new JLabel("CID:"));
	        txtCatId = new JTextField(6);
	        form.add(txtCatId);

	        form.add(new JLabel("CName:"));
	        txtCatName = new JTextField(20);
	        form.add(txtCatName);

	        JButton btnAdd = new JButton("Add");
	        btnAdd.addActionListener(e -> doAddCategory());
	        form.add(btnAdd);

	        JButton btnUpdate = new JButton("Update");
	        btnUpdate.addActionListener(e -> doUpdateCategory());
	        form.add(btnUpdate);

	        JButton btnDelete = new JButton("Delete");
	        btnDelete.addActionListener(e -> doDeleteCategory());
	        form.add(btnDelete);

	        JButton btnRefresh = new JButton("Refresh");
	        btnRefresh.addActionListener(e -> refreshCategoryTable());
	        form.add(btnRefresh);

	        panel.add(form, BorderLayout.NORTH);

	        catModel = new DefaultTableModel(new Object[]{"CID","CName"},0) {
	            @Override public boolean isCellEditable(int r,int c){ return false; }
	        };
	        catTable = new JTable(catModel);
	        catTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        catTable.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e){
	                int r = catTable.getSelectedRow();
	                if(r>=0){
	                    txtCatId.setText(String.valueOf(catModel.getValueAt(r,0)));
	                    txtCatName.setText(String.valueOf(catModel.getValueAt(r,1)));
	                }
	            }
	        });

	        panel.add(new JScrollPane(catTable), BorderLayout.CENTER);
	        return panel;
	    }

	    private void doAddCategory(){
	        try {
	            String name = txtCatName.getText().trim();
	            if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Enter category name"); return; }
	            Category c = new Category(0, name);
	            categoryService.insertCategory(c);
	            JOptionPane.showMessageDialog(this,"Category added.");
	            refreshCategoryTable();
	            refreshCategoryCombo();
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void doUpdateCategory(){
	        try {
	            int id = Integer.parseInt(txtCatId.getText().trim());
	            String name = txtCatName.getText().trim();
	            Category c = new Category(id, name);
	            categoryService.updateCategory(c);
	            JOptionPane.showMessageDialog(this,"Category updated.");
	            refreshCategoryTable();
	            refreshCategoryCombo();
	        } catch (NumberFormatException nfe) {
	            JOptionPane.showMessageDialog(this,"Enter numeric CID");
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void doDeleteCategory(){
	        try {
	            int id = Integer.parseInt(txtCatId.getText().trim());
	            int conf = JOptionPane.showConfirmDialog(this,"Delete category id "+id+" ?","Confirm",JOptionPane.YES_NO_OPTION);
	            if(conf==JOptionPane.YES_OPTION){
	                categoryService.deleteCategory(id);
	                JOptionPane.showMessageDialog(this,"Category deleted.");
	                refreshCategoryTable();
	                refreshCategoryCombo();
	            }
	        } catch (NumberFormatException nfe) {
	            JOptionPane.showMessageDialog(this,"Enter numeric CID");
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void refreshCategoryTable(){
	        try {
	            catModel.setRowCount(0);
	            List<Category> cats = categoryService.getAllcategories();
	            for(Category c: cats){
	                catModel.addRow(new Object[]{c.getId(), c.getName()});
	            }
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    // ---------------- Product Tab ----------------
	    private JPanel buildProductPanel() {
	        JPanel panel = new JPanel(new BorderLayout(6,6));

	        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        form.add(new JLabel("PID:"));
	        txtProdId = new JTextField(6);
	        form.add(txtProdId);

	        form.add(new JLabel("Name:"));
	        txtProdName = new JTextField(12);
	        form.add(txtProdName);

	        form.add(new JLabel("Price:"));
	        txtProdPrice = new JTextField(8);
	        form.add(txtProdPrice);

	        form.add(new JLabel("Qty:"));
	        txtProdQty = new JTextField(6);
	        form.add(txtProdQty);

	        form.add(new JLabel("Category:"));
	        comboCats = new JComboBox<>();
	        comboCats.setPreferredSize(new Dimension(200,24));
	        form.add(comboCats);

	        JButton btnAdd = new JButton("Add");
	        btnAdd.addActionListener(e -> doAddProduct());
	        form.add(btnAdd);

	        JButton btnUpdate = new JButton("Update");
	        btnUpdate.addActionListener(e -> doUpdateProduct());
	        form.add(btnUpdate);

	        JButton btnDelete = new JButton("Delete");
	        btnDelete.addActionListener(e -> doDeleteProduct());
	        form.add(btnDelete);

	        JButton btnRefresh = new JButton("Refresh");
	        btnRefresh.addActionListener(e -> refreshProductTable());
	        form.add(btnRefresh);

	        panel.add(form, BorderLayout.NORTH);

	        prodModel = new DefaultTableModel(new Object[]{"PID","PName","Price","Quantity","CID"},0){
	            @Override public boolean isCellEditable(int r,int c){ return false; }
	        };
	        prodTable = new JTable(prodModel);
	        prodTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        prodTable.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e){
	                int r = prodTable.getSelectedRow();
	                if(r>=0){
	                    txtProdId.setText(String.valueOf(prodModel.getValueAt(r,0)));
	                    txtProdName.setText(String.valueOf(prodModel.getValueAt(r,1)));
	                    txtProdPrice.setText(String.valueOf(prodModel.getValueAt(r,2)));
	                    txtProdQty.setText(String.valueOf(prodModel.getValueAt(r,3)));
	                    int cid = Integer.parseInt(String.valueOf(prodModel.getValueAt(r,4)));
	                    setComboCategoryById(cid);
	                }
	            }
	        });

	        panel.add(new JScrollPane(prodTable), BorderLayout.CENTER);
	        return panel;
	    }

	    private void doAddProduct(){
	        try {
	            String name = txtProdName.getText().trim();
	            double price = Double.parseDouble(txtProdPrice.getText().trim());
	            int qty = Integer.parseInt(txtProdQty.getText().trim());
	            Category sel = (Category) comboCats.getSelectedItem();
	            int cid = sel!=null ? sel.getId() : 0;
	            if(name.isEmpty()){ JOptionPane.showMessageDialog(this,"Enter product name"); return; }
	            Product p = new Product(0, name, price, qty, cid);
	            productService.insertProduct(p);
	            JOptionPane.showMessageDialog(this,"Product added.");
	            refreshProductTable();
	            refreshShopProducts();
	        } catch (NumberFormatException nfe) {
	            JOptionPane.showMessageDialog(this,"Enter numeric price/quantity");
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void doUpdateProduct(){
	        try {
	            int pid = Integer.parseInt(txtProdId.getText().trim());
	            String name = txtProdName.getText().trim();
	            double price = Double.parseDouble(txtProdPrice.getText().trim());
	            int qty = Integer.parseInt(txtProdQty.getText().trim());
	            Category sel = (Category) comboCats.getSelectedItem();
	            int cid = sel!=null ? sel.getId() : 0;
	            Product p = new Product(pid, name, price, qty, cid);
	            productService.updateProduct(p);
	            JOptionPane.showMessageDialog(this,"Product updated.");
	            refreshProductTable();
	            refreshShopProducts();
	        } catch (NumberFormatException nfe) {
	            JOptionPane.showMessageDialog(this,"Enter numeric values");
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void doDeleteProduct(){
	        try {
	            int pid = Integer.parseInt(txtProdId.getText().trim());
	            int conf = JOptionPane.showConfirmDialog(this,"Delete product id "+pid+" ?","Confirm",JOptionPane.YES_NO_OPTION);
	            if(conf==JOptionPane.YES_OPTION){
	                productService.deleteProduct(pid);
	                JOptionPane.showMessageDialog(this,"Product deleted.");
	                refreshProductTable();
	                refreshShopProducts();
	            }
	        } catch (NumberFormatException nfe) {
	            JOptionPane.showMessageDialog(this,"Enter numeric PID");
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void refreshProductTable(){
	        try {
	            prodModel.setRowCount(0);
	            List<Product> products = productService.getAllProducts();
	            for(Product p: products){
	                prodModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getCategoryId()});
	            }
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void refreshCategoryCombo(){
	        try {
	            comboCats.removeAllItems();
	            List<Category> cats = categoryService.getAllcategories();
	            for(Category c: cats) comboCats.addItem(c);
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void setComboCategoryById(int cid){
	        for(int i=0;i<comboCats.getItemCount();i++){
	            Category c = comboCats.getItemAt(i);
	            if(c.getId()==cid){ comboCats.setSelectedIndex(i); return; }
	        }
	    }

	    // ---------------- Purchase Tab ----------------
	    private JPanel buildPurchasePanel(){
	        JPanel root = new JPanel(new BorderLayout(6,6));

	        shopModel = new DefaultTableModel(new Object[]{"PID","PName","Price","Quantity","CID"},0){
	            @Override public boolean isCellEditable(int r,int c){ return false; }
	        };
	        shopTable = new JTable(shopModel);
	        shopTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	        JScrollPane leftScroll = new JScrollPane(shopTable);
	        leftScroll.setPreferredSize(new Dimension(520,400));
	        root.add(leftScroll, BorderLayout.WEST);

	        JPanel right = new JPanel(new BorderLayout(6,6));

	        // top controls
	        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        top.add(new JLabel("Buy Qty:"));
	        txtBuyQty = new JTextField(5);
	        top.add(txtBuyQty);

	        JButton btnAddToCart = new JButton("Add to Cart");
	        btnAddToCart.addActionListener(e -> doAddToCart());
	        top.add(btnAddToCart);

	        JButton btnRemoveCart = new JButton("Remove");
	        btnRemoveCart.addActionListener(e -> doRemoveCart());
	        top.add(btnRemoveCart);

	        JButton btnClearCart = new JButton("Clear Cart");
	        btnClearCart.addActionListener(e -> doClearCart());
	        top.add(btnClearCart);

	        right.add(top, BorderLayout.NORTH);

	        cartModel = new DefaultTableModel(new Object[]{"PID","PName","Qty","Price","Total"},0){
	            @Override public boolean isCellEditable(int r,int c){ return false; }
	        };
	        cartTable = new JTable(cartModel);
	        JScrollPane cartScroll = new JScrollPane(cartTable);
	        cartScroll.setPreferredSize(new Dimension(520,200));
	        right.add(cartScroll, BorderLayout.CENTER);

	        JPanel bottom = new JPanel(new BorderLayout(6,6));

	        JPanel billInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        billInfo.add(new JLabel("Bill ID:"));
	        lblBillId = new JLabel("-");
	        billInfo.add(lblBillId);

	        billInfo.add(new JLabel("Date/Time:"));
	        lblDateTime = new JLabel("-");
	        billInfo.add(lblDateTime);

	        billInfo.add(new JLabel("Grand:"));
	        lblGrand = new JLabel("0.00");
	        billInfo.add(lblGrand);

	        JButton btnGenerate = new JButton("Generate Bill & Purchase");
	        btnGenerate.addActionListener(e -> doGenerateBill());
	        billInfo.add(btnGenerate);

	        JButton btnSaveBill = new JButton("Save Bill (txt)");
	        btnSaveBill.addActionListener(e -> doSaveBillToFile());
	        billInfo.add(btnSaveBill);

	        bottom.add(billInfo, BorderLayout.NORTH);

	        txtReceipt = new JTextArea(12,40);
	        txtReceipt.setEditable(false);
	        txtReceipt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
	        bottom.add(new JScrollPane(txtReceipt), BorderLayout.CENTER);

	        right.add(bottom, BorderLayout.SOUTH);

	        root.add(right, BorderLayout.CENTER);

	        return root;
	    }

	    private void refreshShopProducts(){
	        try {
	            shopModel.setRowCount(0);
	            List<Product> prods = productService.getAllProducts();
	            for(Product p: prods){
	                shopModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getCategoryId()});
	            }
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void doAddToCart(){
	        int r = shopTable.getSelectedRow();
	        if(r<0){ JOptionPane.showMessageDialog(this,"Select product first"); return; }
	        try {
	            int pid = Integer.parseInt(String.valueOf(shopModel.getValueAt(r,0)));
	            String name = String.valueOf(shopModel.getValueAt(r,1));
	            double price = Double.parseDouble(String.valueOf(shopModel.getValueAt(r,2)));
	            int avail = Integer.parseInt(String.valueOf(shopModel.getValueAt(r,3)));
	            int qty = Integer.parseInt(txtBuyQty.getText().trim());
	            if(qty<=0){ JOptionPane.showMessageDialog(this,"Enter qty > 0"); return; }
	            if(qty>avail){ JOptionPane.showMessageDialog(this,"Only "+avail+" available"); return; }

	            double total = price * qty;
	            cartModel.addRow(new Object[]{pid, name, qty, price, total});
	            cartProductIds.add(pid);
	            cartQuantities.add(qty);
	            updateGrand();
	        } catch (NumberFormatException nfe){
	            JOptionPane.showMessageDialog(this,"Enter numeric qty");
	        }
	    }

	    private void doRemoveCart(){
	        int r = cartTable.getSelectedRow();
	        if(r<0){ JOptionPane.showMessageDialog(this,"Select cart row"); return; }
	        cartModel.removeRow(r);
	        if(r < cartProductIds.size()){
	            cartProductIds.remove(r);
	            cartQuantities.remove(r);
	        }
	        updateGrand();
	    }

	    private void doClearCart(){
	        cartModel.setRowCount(0);
	        cartProductIds.clear();
	        cartQuantities.clear();
	        updateGrand();
	        txtReceipt.setText("");
	        lblBillId.setText("-");
	        lblDateTime.setText("-");
	    }

	    private void updateGrand(){
	        double g = 0;
	        for(int i=0;i<cartModel.getRowCount();i++){
	            g += Double.parseDouble(String.valueOf(cartModel.getValueAt(i,4)));
	        }
	        lblGrand.setText(String.format("%.2f", g));
	    }

	    private void doGenerateBill(){
	        if(cartModel.getRowCount()==0){ JOptionPane.showMessageDialog(this,"Cart empty"); return; }

	        String uid = UUID.randomUUID().toString().substring(0,8).toUpperCase();
	        LocalDateTime now = LocalDateTime.now();
	        String dt = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	        String billId = "BILL-" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "-" + uid;

	        lblBillId.setText(billId);
	        lblDateTime.setText(dt);

	        StringBuilder sb = new StringBuilder();
	        sb.append("      >>>>>  SHOP RECEIPT  <<<<<\n");
	        sb.append("Bill ID: ").append(billId).append("\n");
	        sb.append("Date: ").append(dt).append("\n");
	        sb.append("----------------------------------------\n");
	        sb.append(String.format("%-20s %5s %8s %8s\n","NAME","QTY","PRICE","TOTAL"));
	        sb.append("----------------------------------------\n");
	        double grand = 0;
	        for(int i=0;i<cartModel.getRowCount();i++){
	            String name = String.valueOf(cartModel.getValueAt(i,1));
	            int q = Integer.parseInt(String.valueOf(cartModel.getValueAt(i,2)));
	            double price = Double.parseDouble(String.valueOf(cartModel.getValueAt(i,3)));
	            double total = Double.parseDouble(String.valueOf(cartModel.getValueAt(i,4)));
	            sb.append(String.format("%-20s %5d %8.2f %8.2f\n", name, q, price, total));
	            grand += total;
	        }
	        sb.append("----------------------------------------\n");
	        sb.append(String.format("Grand Total: %.2f\n", grand));
	        sb.append("----------------------------------------\n");
	        sb.append("Thank you for shopping!\n");

	        txtReceipt.setText(sb.toString());
	        lblGrand.setText(String.format("%.2f", grand));

	        int conf = JOptionPane.showConfirmDialog(this, "Confirm Purchase? Bill ID: " + billId, "Confirm", JOptionPane.YES_NO_OPTION);
	        if(conf != JOptionPane.YES_OPTION){
	            return;
	        }

	        try {
	            productService.purchaseProducts(new ArrayList<>(cartProductIds), new ArrayList<>(cartQuantities));
	            JOptionPane.showMessageDialog(this,"Purchase success. Bill ID: "+billId);
	            doClearCart();
	            refreshShopProducts();
	            refreshProductTable();
	        } catch (SQLException ex) {
	            showError(ex);
	        }
	    }

	    private void doSaveBillToFile(){
	        String text = txtReceipt.getText();
	        if(text == null || text.trim().isEmpty()){
	            JOptionPane.showMessageDialog(this,"No receipt to save.");
	            return;
	        }
	        // save file in user's home directory or current working dir with timestamp
	        String fileName = "bill_" + System.currentTimeMillis() + ".txt";
	        File file = new File(System.getProperty("user.home"), fileName);
	        try (FileWriter fw = new FileWriter(file)) {
	            fw.write(text);
	            JOptionPane.showMessageDialog(this,"Bill saved to: " + file.getAbsolutePath());
	        } catch (IOException e) {
	            showError(e);
	        }
	    }

	    private void showError(Exception ex){
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }

	    public static void main(String[] args){
	        SwingUtilities.invokeLater(() -> {
	            MainGUI ui = new MainGUI();
	            ui.setVisible(true);
	        });
	    }
	

}
