package model;

import java.io.Serializable;
import java.util.*;

public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CartItem> items = new ArrayList<>();
    private double totalAmount;
    private Date date = new Date();

    public void addItem(CartItem item) {
        items.add(item);
        calculateTotal();
    }

    private void calculateTotal() {
        totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getTotalPrice();
        }
    }

    public List<CartItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public Date getDate() { return date; }
}