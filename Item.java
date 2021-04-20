import java.util.*;
import java.io.*;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;
    private float ppu;

    public Item(Product prod, int qty) {
        product = prod;
        quantity = qty;
        ppu = prod.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity) {
        quantity = newQuantity;
    }

    public float getPricePerUnit() {
        return ppu;
    }

    public String toString() {
        return "Product Name: " + product.getName() + "Product Quantity: " + quantity + "PricePerunit: " + ppu;
    }
}
