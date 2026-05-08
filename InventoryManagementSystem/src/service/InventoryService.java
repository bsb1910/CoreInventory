package service;

import model.Product;
import util.FileHandler;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {

    private List<Product> products;
    private final String FILE = "resources/inventory.dat";

    public InventoryService() {
        load();
    }
@SuppressWarnings("unchecked")
    private void load() {
        Object data = FileHandler.loadFromFile(FILE);
        products = (data != null) ? (List<Product>) data : new ArrayList<>();
    }

    private void save() {
        FileHandler.saveToFile(products, FILE);
    }

    public void addProduct(Product p) {
        products.add(p);
        save();
    }

    public boolean updateProduct(int id, String name, double price, int qty) {
        for (Product p : products) {
            if (p.getId() == id) {
                p.setName(name);
                p.setPrice(price);
                p.setQuantity(qty);
                save();
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (removed) save();
        return removed;
    }

    public List<Product> getAll() {
        return products;
    }

    public Product searchById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Product> searchByName(String name) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> lowStock() {
        return products.stream().filter(Product::isLowStock).collect(Collectors.toList());
    }

    public List<Product> sortAsc() {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }

    public List<Product> sortDesc() {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }
}