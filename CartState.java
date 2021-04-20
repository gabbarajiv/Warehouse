import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CartState extends WarehouseState implements ActionListener {

    private static Warehouse warehouse;
    private static CartState instance;
    private JFrame frame;
    private static CartState cartState;
    private static Cart cart;
    private Security security = new Security();
    private LinkedList<Cart> items = new LinkedList<>();

    private AbstractButton exitButton;
    private AbstractButton viewCartButton;
    private AbstractButton addCartButton;
    private AbstractButton changeCartQuantityButton;
    private AbstractButton removefromCartButton;
    private AbstractButton saveButton;

    private CartState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static CartState instance() {
        if (instance == null) {
            instance = new CartState();
        }
        return instance;
    }

    private void viewCart() {
        String id = JOptionPane.showInputDialog(frame, "Enter the customer ID: ");
        Cart shoppingCart = warehouse.getCart(id);

        if (shoppingCart != null) {

            JOptionPane.showMessageDialog(frame, "Client's Cart" + shoppingCart);
        } else {
            JOptionPane.showMessageDialog(frame, "Could not retrieve cart");
        }
    }

    private void addToCart() {
        Cart result;
        String clientId = JOptionPane.showInputDialog(frame, "Enter the client ID: ");
        String productId = JOptionPane.showInputDialog(frame, "Enter the Product ID: ");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter a quantity: "));
        result = warehouse.addToCart(clientId, productId, quantity);
        if (result == null) {
            JOptionPane.showMessageDialog(frame, "Could not add that Item to that Cart");
        } else {
            JOptionPane.showMessageDialog(frame, result);
        }

    }

    private void changeCartQuantity() {
        String id = (JOptionPane.showInputDialog(frame, "Enter client id"));
        Client result;
        Cart shoppingcart = warehouse.getCart(id);
        String pId = (JOptionPane.showInputDialog(frame, "Enter product id"));
        Product product = warehouse.searchGetProduct(pId);
        int quantity = 0;

        if (shoppingcart != null && product != null) {
            quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter new quantity"));
        }
        result = warehouse.changeQuantity(id, pId, quantity);
        if (result == null) {
            System.out.println("Quantity could not be changed");
        } else {
            System.out.println(shoppingcart);
        }
    }

    public void removeFromCart() {
        String productID = JOptionPane.showInputDialog(frame, "Enter product Id");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter quantity"));

        cart.removeProduct(productID, quantity);
    }

    public void exit() {

        (WarehouseContext.instance()).changeState(3);
    }

    private void save() {
        if (Warehouse.save()) {
            JOptionPane.showMessageDialog(frame,
                    "The warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            JOptionPane.showMessageDialog(frame, "Error While saving \n");
        }
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.exitButton)) {
            this.exit();
        } else if (event.getSource().equals(this.viewCartButton))
            this.viewCart();
        else if (event.getSource().equals(this.addCartButton))
            this.addToCart();
        else if (event.getSource().equals(this.changeCartQuantityButton))
            this.changeCartQuantity();
        else if (event.getSource().equals(this.removefromCartButton))
            this.removeFromCart();
        else if (event.getSource().equals(this.saveButton)) {
            this.save();
        }
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());

        exitButton = new JButton("Exit");
        viewCartButton = new JButton("View Cart");
        addCartButton = new JButton("Add to Cart");
        changeCartQuantityButton = new JButton("Change Cart Quantity");
        removefromCartButton = new JButton("Remove from Cart");
        saveButton = new JButton("save");

        exitButton.addActionListener(this);
        viewCartButton.addActionListener(this);
        addCartButton.addActionListener(this);
        changeCartQuantityButton.addActionListener(this);
        removefromCartButton.addActionListener(this);
        saveButton.addActionListener(this);

        frame.getContentPane().add(this.exitButton);
        frame.getContentPane().add(this.viewCartButton);
        frame.getContentPane().add(this.addCartButton);
        frame.getContentPane().add(this.changeCartQuantityButton);
        frame.getContentPane().add(this.removefromCartButton);
        frame.getContentPane().add(this.saveButton);

        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();

    }
}
