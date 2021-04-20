import java.util.*;
import java.io.*;

public class Product implements Serializable {

    private String productId;
    private String productName;
    private float price;
    private int quantity;
    private int waitlistQuantity;

    private static final long serialVersionUID = 1L;
    private static final String PRODUCT_STRING = "P";
    private LinkedList waitListProducts = new LinkedList<>();
    private LinkedList shipmentRecords = new LinkedList<>();

    public Product(String name, float price, int quantity) {
        this.productName = name;
        this.price = price;
        this.quantity = quantity;
        this.productId = PRODUCT_STRING + (ProductIdServer.instance()).getId();
        this.waitlistQuantity = 0;
    }

    public Product() {

    }

    public String getName() {
        return productName;
    }

    public void setName(String newName) {
        productName = newName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void clearWaitlist() {
        Iterator waitlistItems = getWaitlistItems();
        WaitList temp;
        while (waitlistItems.hasNext()) {
            temp = (WaitList) waitlistItems.next();
            if (temp.isFilled()) {
                waitlistItems.remove();
            }
        }
        setWaitlistQuantity();
    }

    public void setQuantity(int newQuantity) {
        quantity = newQuantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(String newPrice) {
        price = Float.parseFloat(newPrice);
    }

    public String getId() {
        return productId;
    }

    public boolean equals(String id) {
        return this.productId.equals(id);
    }

    public Iterator getWaitlistItems() {
        return waitListProducts.iterator();
    }

    public int getWaitlistQuantity() {
        return waitlistQuantity;
    }

    public Iterator getShipmentRecords() {
        return shipmentRecords.iterator();
    }

    public void setWaitlistQuantity() {
        waitlistQuantity = waitListProducts.size();
    }

    public void addToWaitlist(WaitList newWaitlistItem) {
        waitListProducts.add(newWaitlistItem);
        waitlistQuantity = waitListProducts.size();
    }

    public void addShipmentRecord(ShipmentRecord record) {
        shipmentRecords.add(record);
    }

    public String

            toString() {
        return String.format("Product's Name:  " + productName + "    Product's id:  " + productId
                + "    Prodcuct's price:  " + price + "    Product's quantity:  " + quantity);
    }

}
