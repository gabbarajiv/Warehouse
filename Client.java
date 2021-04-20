import java.util.*;
import java.io.*;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientname;
    private String clientaddress;
    private String clientphone;
    private String clientid;
    private Cart cart;
    private float clientsbalance;
    private int waitlistQuantity;

    private LinkedList waitlistProducts = new LinkedList();
    private LinkedList transactionList = new LinkedList();
    private static final String CLIENT_STRING = "C";

    public Client(String name, String address, String phone) {
        this.clientname = name;
        this.clientaddress = address;
        this.clientid = CLIENT_STRING + (ClientIdServer.instance()).getId();
        this.cart = new Cart(this);
        this.clientphone = phone;
        this.waitlistQuantity = 0;
        this.clientsbalance = 0;
    }

    public Client() {
    }

    public String getName() {
        return clientname;
    }

    public void setName(String newName) {
        clientname = newName;
    }

    public String getAddress() {
        return clientaddress;
    }

    public void setAddress(String newAddress) {
        clientaddress = newAddress;
    }

    public String getId() {
        return clientid;
    }

    public Cart getCart() {
        return cart;
    }

    public void setcart(Cart newCart) {
        this.cart = newCart;
    }

    public boolean equals(String id) {
        return this.clientid.equals(id);
    }

    public Iterator getWaitlistProducts() {
        return waitlistProducts.iterator();
    }

    public float getClientsBalance() {
        return this.clientsbalance;
    }

    public boolean addToCart(Product product, int quantity) {
        if (this.cart.add(product, quantity)) {
            System.out.println("Product successfully added to cart");
            return true;
        } else {
            return false;
        }
    }

    public void addWaitlistProduct(WaitList newWaitlistItem) {
        waitlistProducts.add(newWaitlistItem);
        waitlistQuantity = waitlistProducts.size();
    }

    public String processCharge(Product prod, int quantity) {
        String invoice = String.format("%-10s %-5d %-10.2f    Total: $%-17.2f", prod.getName(), quantity,
                prod.getPrice(), quantity * prod.getPrice());
        this.clientsbalance -= quantity * prod.getPrice();
        return invoice;
    }

    public void addTransaction(Transaction newTransaction) {
        transactionList.add(newTransaction);
    }

    public String makeOrder() {
        String invoice = cart.processOrder();
        return invoice;
    }

    public void processPayment(float amount) {
        clientsbalance = clientsbalance - amount;
    }

    public void clearWaitlist() {
        Iterator waitlistItems = getWaitlistProducts();
        WaitList temp;
        while (waitlistItems.hasNext()) {
            temp = (WaitList) waitlistItems.next();
            if (temp.isFilled()) {
                waitlistItems.remove();
            }
        }
    }

    public boolean updateCart(Product product, int quantity) {
        if (this.cart.update(product, quantity) && quantity != 0) {
            System.out.println("cart updated");
            return true;
        } else {
            return false;
        }
    }

    public Iterator getTransactions() {
        return transactionList.iterator();

    }

    public String toString() {
        return String.format("Client's Name:  " + clientname + "     Client's id:  " + clientid
                + "      Client's address:  " + clientaddress + "      Client's phone:  " + clientphone
                + "    Client's Account Balance:  " + clientsbalance);
    }

}