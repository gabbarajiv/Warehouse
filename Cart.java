import java.util.*;
import java.io.*;

public class Cart implements Serializable {
    private LinkedList cartlist = new LinkedList();
    private static Cart cart;
    private Client client;
    private static final long serialVersionUID = 1L;

    public Cart(Client clnt) {
        client = clnt;
    }

    public boolean add(Product product, int quantity) {
        Iterator cartLineItems = getClientsCart();
        while (cartLineItems.hasNext()) {
            // The relationship between LineItem and product is that a product can
            // appear on zero, one, or many LineItems, but each LineItem refers to exactly
            // one product.
            Item tempo = (Item) cartLineItems.next();
            if (tempo.getProduct().getId().equals(product.getId())) {

                tempo.setQuantity(tempo.getQuantity() + quantity);
                return true;
            }
        }
        Item lineLineItem = new Item(product, quantity);
        return cartlist.add(lineLineItem);

    }

    public Iterator getClientsCart() {
        return cartlist.iterator();
    }

    private void removeAllCartLineItems() {
        cartlist.clear();
    }

    public String processOrder() {
        float total = 0;
        String invoice = " ";
        int numberOfLineItems = 0;
        float oneLineItemTotalCost;

        Iterator cartLineItems = getClientsCart();

        while (cartLineItems.hasNext()) {
            Item tempo1 = (Item) cartLineItems.next();
            Product tempo1product = tempo1.getProduct();
            int inWarehouse = tempo1product.getQuantity();
            int cartQuantity = tempo1.getQuantity();

            if (cartQuantity <= inWarehouse) {
                oneLineItemTotalCost = cartQuantity * tempo1.getPricePerUnit();
                total += oneLineItemTotalCost;
                invoice = "Total: " + tempo1.toString() + oneLineItemTotalCost + invoice;
                tempo1product.setQuantity(inWarehouse - cartQuantity);
                numberOfLineItems += tempo1.getQuantity();
            }

            else if (tempo1product.getQuantity() > 0) {
                oneLineItemTotalCost = inWarehouse * tempo1product.getPrice();
                total += oneLineItemTotalCost;
                tempo1.setQuantity(inWarehouse);
                invoice = "Total: " + tempo1.toString() + oneLineItemTotalCost + invoice;
                int waitlistQuantity = cartQuantity - inWarehouse;
                WaitList newWaitList = new WaitList(client, tempo1product, waitlistQuantity);
                client.addWaitlistProduct(newWaitList);
                tempo1product.addToWaitlist(newWaitList);
                tempo1product.setQuantity(waitlistQuantity - cartQuantity + inWarehouse);
                numberOfLineItems += tempo1.getQuantity();

            } else {
                WaitList newWaitList = new WaitList(client, tempo1product, cartQuantity);
                client.addWaitlistProduct(newWaitList);
                tempo1product.addToWaitlist(newWaitList);

            }

        }
        String orderInfo = numberOfLineItems + "Products";
        Transaction newTransaction = new Transaction(orderInfo, total);

        client.processPayment(total);
        client.addTransaction(newTransaction);

        removeAllCartLineItems();

        invoice = invoice + newTransaction.toString();
        return invoice;

    }

    public String toString() {
        String cart = " ";
        Iterator LineItems = getClientsCart();
        while (LineItems.hasNext()) {
            cart = cart + LineItems.next().toString() + "\n";
        }
        return cart;
    }

    public boolean update(Product product, int quantity) {
        Iterator cartItems = getClientsCart();
        while (cartItems.hasNext()) {

            Item temp = (Item) cartItems.next();
            if (temp.getProduct().getId().equals(product.getId())) {
                temp.setQuantity(quantity);
                return true;
            }
        }
        Item items = new Item(product, quantity);
        return cartlist.add(items);
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(cart);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean removeProduct(String productID, int quantity) {
        Iterator cartItems = getClientsCart();
        while (cartItems.hasNext()) {
            Item temp = (Item) cartItems.next();
            if (temp.getProduct().getId().equals(productID)) {
                cartlist.remove(temp);
                return true;
            }
        }
        return false;
    }

    private static void readObject(java.io.ObjectInputStream input) {
        try {
            if (cart != null) {
                return;
            } else {
                input.defaultReadObject();
                if (cart == null) {
                    cart = (Cart) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

}