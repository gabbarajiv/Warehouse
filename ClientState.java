import java.util.*;
import java.io.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ClientState extends WarehouseState implements ActionListener {

    private static ClientState instance;

    private WarehouseContext context;
    private static Warehouse warehouse;
    private JFrame frame;

    private AbstractButton exitButton;
    private AbstractButton viewClientButton;
    private AbstractButton showProductsButton;
    private AbstractButton transactionListButton;
    private AbstractButton viewCartButton;
    private AbstractButton displayWaitlistButton;
    private AbstractButton logoutButton;
    private AbstractButton saveButton;

    private ClientState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static ClientState instance() {
        if (instance == null) {
            instance = new ClientState();
        }
        return instance;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.exitButton))
            this.exit();
        else if (event.getSource().equals(this.viewClientButton))
            this.searchClient();
        else if (event.getSource().equals(this.showProductsButton))
            this.showProducts();
        else if (event.getSource().equals(this.transactionListButton))
            this.getClientTransactions();
        else if (event.getSource().equals(this.viewCartButton))
            this.viewCart();
        else if (event.getSource().equals(this.displayWaitlistButton))
            this.getClientTransactions();
        else if (event.getSource().equals(this.logoutButton))
            this.logOUT();
        else if (event.getSource().equals(this.saveButton)) {
            this.save();
        }

    }

    public void searchClient() {
        String client = warehouse.searchClient(WarehouseContext.instance().getUser()).toString();
        JOptionPane.showMessageDialog(frame, client);
    }

    private void viewCart() {
        (WarehouseContext.instance()).changeState(4);
    }

    private void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()) {
            Product temp = (Product) allProducts.next();
            JOptionPane.showMessageDialog(frame, temp);
        }
    }

    public void exit() {

        (WarehouseContext.instance()).changeState(3);
        save();
    }

    private void getClientTransactions() {
        Iterator transaction = warehouse.getClientTransactions(WarehouseContext.instance().getUser());
        if (transaction != null) {

            while (transaction.hasNext()) {
                Transaction temp = (Transaction) transaction.next();
                JOptionPane.showMessageDialog(frame, temp);
            }
        } else
            JOptionPane.showMessageDialog(frame, "Client not found ");

    }

    private void save() {
        if (Warehouse.save()) {
            JOptionPane.showMessageDialog(frame,
                    "The warehouse has been successfully saved in the file WarehouseData \n");
        } else {
            JOptionPane.showMessageDialog(frame, "Error While saving \n");
        }
    }

    private void getClientWaitlist() {
        Iterator waitlist = warehouse.getWaitlistClient(WarehouseContext.instance().getUser());
        if (waitlist != null) {

            while (waitlist.hasNext()) {
                JOptionPane.showMessageDialog(frame, waitlist.next());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Client not found");
        }
    }

    public void logOUT() {
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.ISCLERK) { // stem.out.println(" going to clerk
                                                                                    // \n ");
            (WarehouseContext.instance()).changeState(1); // exit with a code 1
        } else if (WarehouseContext.instance().getLogin() == WarehouseContext.ISCLIENT) { // stem.out.println(" going to
                                                                                          // login \n");
            (WarehouseContext.instance()).changeState(2); // exit with a code 2
        } else
            (WarehouseContext.instance()).changeState(3); // exit code 2, indicates error
    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());
        exitButton = new JButton("Exit");
        viewClientButton = new JButton("View Client");
        showProductsButton = new JButton("Show Products");
        transactionListButton = new JButton("Show transactions");
        viewCartButton = new JButton("View Cart");
        displayWaitlistButton = new JButton("Display Waitlist");
        logoutButton = new JButton("logout");
        saveButton = new JButton("save");

        exitButton.addActionListener(this);
        viewClientButton.addActionListener(this);
        showProductsButton.addActionListener(this);
        transactionListButton.addActionListener(this);
        viewCartButton.addActionListener(this);
        displayWaitlistButton.addActionListener(this);
        logoutButton.addActionListener(this);
        saveButton.addActionListener(this);

        frame.getContentPane().add(this.exitButton);
        frame.getContentPane().add(this.viewClientButton);
        frame.getContentPane().add(this.showProductsButton);
        frame.getContentPane().add(this.transactionListButton);
        frame.getContentPane().add(this.viewCartButton);
        frame.getContentPane().add(this.displayWaitlistButton);
        frame.getContentPane().add(this.logoutButton);
        frame.getContentPane().add(this.saveButton);

        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();

    }

}