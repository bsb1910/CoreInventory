package ui;

import model.Product;
import service.InventoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {

    private JTextField idField, nameField, priceField, quantityField, searchField;
    private InventoryService service;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> sortBox;

    public ProductPanel() {
        service = new InventoryService();

        setLayout(new BorderLayout(10, 10));

        // 🔹 FORM PANEL
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Product ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        // 🔹 BUTTON PANEL (CRUD)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(buttonPanel);

        // 🔹 SEARCH + FILTER PANEL
        JPanel topPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JButton lowStockButton = new JButton("Low Stock");

        String[] sortOptions = {"Sort Price ↑", "Sort Price ↓"};
        sortBox = new JComboBox<>(sortOptions);

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(lowStockButton);
        topPanel.add(sortBox);

        // 🔹 COMBINE FORM + SEARCH
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(topPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // 🔹 TABLE
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Price", "Quantity"}, 0
        );

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 LOAD DATA
        loadTable();

        // 🔹 BUTTON ACTIONS

        // ➕ ADD
        addButton.addActionListener(e -> addProduct());

        // ✏️ UPDATE
        updateButton.addActionListener(e -> updateProduct());

        // ❌ DELETE
        deleteButton.addActionListener(e -> deleteProduct());

        // 🔍 SEARCH
        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            List<Product> results = service.searchByName(text);
            updateTable(results);
        });

        // ⚠️ LOW STOCK
        lowStockButton.addActionListener(e -> {
            List<Product> low = service.lowStock();
            updateTable(low);
        });

        // 🔽 SORT
        sortBox.addActionListener(e -> {
            String selected = (String) sortBox.getSelectedItem();

            if (selected.contains("↑")) {
                updateTable(service.sortAsc());
            } else {
                updateTable(service.sortDesc());
            }
        });

        // 📋 TABLE CLICK → FILL FORM
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
    }

    // ➕ ADD
    private void addProduct() {
        try {
            Product p = getFormData();
            service.addProduct(p);

            JOptionPane.showMessageDialog(this, "Product Added");
            clearFields();
            loadTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    // ✏️ UPDATE
    private void updateProduct() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(quantityField.getText());

            boolean updated = service.updateProduct(id, name, price, qty);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Product Updated");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this, "Product Not Found");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    // ❌ DELETE
    private void deleteProduct() {
        try {
            int id = Integer.parseInt(idField.getText());

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this product?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = service.deleteProduct(id);

                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Product Deleted");
                    clearFields();
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Product Not Found");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID");
        }
    }

    // 📊 LOAD TABLE (ALL)
    private void loadTable() {
        updateTable(service.getAll());
    }

    // 🔄 UPDATE TABLE WITH LIST
    private void updateTable(List<Product> list) {
        tableModel.setRowCount(0);

        for (Product p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getQuantity()
            });
        }
    }

    // 📋 FILL FORM FROM TABLE
    private void fillFormFromTable() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            priceField.setText(tableModel.getValueAt(row, 2).toString());
            quantityField.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    // 🧾 GET FORM DATA
    private Product getFormData() {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int qty = Integer.parseInt(quantityField.getText());

        return new Product(id, name, price, qty);
    }

    // 🧹 CLEAR FORM
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }
}