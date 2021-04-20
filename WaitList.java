import java.io.*;

public class WaitList implements Serializable {

    private Client client;
    private Product product;
    private int quantity;
    private boolean filled;
    private static final long serialVersionUID = 1L;

    public WaitList(Client member, Product product, int quantity) {
        this.client = member;
        this.quantity = quantity;
        this.product = product;
        this.filled = false;
    }

    public Client getClient() {
        return client;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int q) {
        this.quantity -= q;
    }

    public boolean isFilled() {
        return filled;
    }

    public boolean setWfilled() {
        this.filled = true;
        return true;
    }

    public String toString() {

        return "ClientID:  " + client.getId() + "     ProductID:  " + product.getId() + "     Quantity:  " + quantity;
    }
}
