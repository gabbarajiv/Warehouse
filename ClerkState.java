import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.text.DateFormat.getDateInstance;

public class ClerkState extends WarehouseState implements ActionListener {

    private static Warehouse warehouse;
    private WarehouseContext context;
    private static ClerkState instance;
    private JFrame frame;

    private AbstractButton exitButton;
    private AbstractButton addClientButton;
    private AbstractButton showProductButton;
    private AbstractButton queryButton;
    private AbstractButton becomeClientButton;
    private AbstractButton displayWaitlistButton;
    private AbstractButton receiveShipmentButton;
    private AbstractButton logoutButton;
    private AbstractButton saveButton;

    private ClerkState() {
        super();
        warehouse = Warehouse.instance();
        // context = WarehouseContext.instance();
    }

    public static ClerkState instance() {
        if (instance == null) {
            instance = new ClerkState();
        }
        return instance;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.exitButton)) {
            this.exit();
        } else if (event.getSource().equals(this.addClientButton))
            this.addClient();
        else if (event.getSource().equals(this.showProductButton))
            this.showProducts();
        else if (event.getSource().equals(this.queryButton))
            this.showQuery();
        else if (event.getSource().equals(this.becomeClientButton))
            this.becomeClient();
        else if (event.getSource().equals(this.displayWaitlistButton))
            this.showProductsForWaitList();
        else if (event.getSource().equals(this.receiveShipmentButton))
            this.receiveShipment();
        else if (event.getSource().equals(this.logoutButton))
            this.logout();
        else if (event.getSource().equals(this.saveButton)) {
            this.save();
        }
    }

    public void exit() {

        (WarehouseContext.instance()).changeState(3);
		save();
    }

    private void save() {
        if (Warehouse.save()) {
            JOptionPane.showMessageDialog(frame,
                    "The warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            JOptionPane.showMessageDialog(frame, "Error While saving \n");
        }
    }

    private void addClient() {
        String name = JOptionPane.showInputDialog(frame, "Please enter the client name");
        String address = JOptionPane.showInputDialog(frame, "Please enter the client address");
        String phone = JOptionPane.showInputDialog(frame, "Please enter the client phone number ");
        Client result = warehouse.addClient(name, address, phone);
        if (result == null) {
            JOptionPane.showMessageDialog(frame, "could not add client");
        } else {
            JOptionPane.showMessageDialog(frame, result);
        }
    }

    public void receiveShipment() {
        String invoice;
        int reqQuantity;
        WaitList wList;
        boolean lastitemfull = true;
        String pId = JOptionPane.showInputDialog(frame, "Enter the ProductId");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the Quantity"));
        Iterator waitlistitems = warehouse.getWaitlistProd(pId);
        while (waitlistitems.hasNext() && quantity > 0) {
            JOptionPane.showInputDialog(frame, quantity);
            wList = (WaitList) waitlistitems.next();
            JOptionPane.showInputDialog(frame, wList);
            if (yesOrNo("Fill this Order??")) {
                if (wList.getQuantity() > quantity) {
                    reqQuantity = quantity;
                    wList.updateQuantity(wList.getQuantity() - quantity);
                } else {
                    reqQuantity = wList.getQuantity();
                    wList.setWfilled();
                }
                invoice = warehouse.fillClientWaitlist(wList, reqQuantity);
                System.out.println(invoice);
                quantity -= reqQuantity;
                lastitemfull = wList.isFilled();
            }
        }
        boolean finished = false;
        if (!waitlistitems.hasNext() && lastitemfull) {
            finished = true;
        }

        warehouse.clearWaitlist(pId);

        if (finished) {
            Product prod = warehouse.setProductQuantity(pId, quantity);
            System.out.println("Shipment added to inventory");
            System.out.println(prod);
        } else {
            System.out.println("Couldn't fill all waitlisted orders for this product");
            System.out.println("Unfilled waitlist orders:   \n");
            Iterator wlist = warehouse.getWaitlistProd(pId);
            if (wlist != null) {
                System.out.println("Waitlist");
                while (wlist.hasNext()) {
                    System.out.println(wlist.next());
                }
            }
        }
    }

    private void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        JOptionPane.showInputDialog(frame, "ProductList");
        while (allProducts.hasNext()) {
            Product temp = (Product) allProducts.next();
            JOptionPane.showInputDialog(frame, temp);
        }
    }

    public void showQuery() {

        (WarehouseContext.instance()).setLogin(WarehouseContext.ISQUERY);
        (WarehouseContext.instance()).changeState(5);
    }

    public void becomeClient() {
        String userID = JOptionPane.showInputDialog(frame, "Please input the client id: ");
        if (Warehouse.instance().getClientInfo(userID) != null) {
            (WarehouseContext.instance()).setUser(userID);
            (WarehouseContext.instance()).changeState(2);
        } else
            JOptionPane.showInputDialog(frame, "Invalid user id.");
        (WarehouseContext.instance()).changeState(2);

    }

    public void logout() {
        (WarehouseContext.instance()).changeState(0); // exit with a code 0
    }

    public void showProductsForWaitList() {
        Iterator productList = warehouse.getProducts();
        while (productList.hasNext()) {
            Product tempProduct = (Product) productList.next();
            if (tempProduct.getWaitlistQuantity() > 0) {
                JOptionPane.showInputDialog(frame, tempProduct);

            }
        }
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());

        exitButton = new JButton("Exit");
        addClientButton = new JButton("Add Client");
        showProductButton = new JButton("Show Products");
        queryButton = new JButton("Query Menu");
        becomeClientButton = new JButton("Become Client");
        displayWaitlistButton = new JButton("Display Waitlist");
        receiveShipmentButton = new JButton("Show Products");
        logoutButton = new JButton("Query Menu");
        saveButton = new JButton("save");

        exitButton.addActionListener(this);
        addClientButton.addActionListener(this);
        showProductButton.addActionListener(this);
        queryButton.addActionListener(this);
        becomeClientButton.addActionListener(this);
        displayWaitlistButton.addActionListener(this);
        receiveShipmentButton.addActionListener(this);
        logoutButton.addActionListener(this);
        saveButton.addActionListener(this);

        frame.getContentPane().add(this.exitButton);
        frame.getContentPane().add(this.addClientButton);
        frame.getContentPane().add(this.showProductButton);
        frame.getContentPane().add(this.queryButton);
        frame.getContentPane().add(this.becomeClientButton);
        frame.getContentPane().add(this.displayWaitlistButton);
        frame.getContentPane().add(this.receiveShipmentButton);
        frame.getContentPane().add(this.logoutButton);
        frame.getContentPane().add(this.saveButton);

        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();

    }

}
