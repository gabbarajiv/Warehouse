import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QueryState extends WarehouseState implements ActionListener {

    private static QueryState instance;
    private WarehouseContext context;
    private static Warehouse warehouse;

    private JFrame frame;
    private AbstractButton exitButton;
    private AbstractButton getOutstandingClientsButton;
    private AbstractButton showClientsButton;
    private AbstractButton logoutButton;

    private QueryState() {
        super();
        warehouse = Warehouse.instance();
    }

    public static QueryState instance() {
        if (instance == null) {
            instance = new QueryState();
        }
        return instance;
    }

    private void getOutstandingClients() {
        Iterator allClients = warehouse.getOutstandingClients();

        if (allClients != null) {

            while (allClients.hasNext()) {
                Client temp = (Client) allClients.next();
                JOptionPane.showMessageDialog(frame, temp);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Unable to Process That Transaction");
        }
    }

    public void exit() {

        (WarehouseContext.instance()).changeState(3);
    }

    private void showClients() {
        Iterator allClients = warehouse.getClients();

        if (allClients.hasNext()) {
            Client temp = (Client) allClients.next();
            JOptionPane.showMessageDialog(frame, temp);
        }
        else {
            JOptionPane.showMessageDialog(frame, "No clients in system");
        }

    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.exitButton)) {
            this.exit();
        } else if (event.getSource().equals(this.getOutstandingClientsButton))
            this.getOutstandingClients();
        else if (event.getSource().equals(this.showClientsButton))
            this.showClients();
        else if (event.getSource().equals(this.logoutButton))
            this.logOUT();

    }

    public void logOUT() {

        (WarehouseContext.instance()).changeState(1); // exit with a code 0

    }

    public void run() {
        frame = WarehouseContext.instance().getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new FlowLayout());

        exitButton = new JButton("Exit");
        getOutstandingClientsButton = new JButton("Show Outstanding Clients");
        showClientsButton = new JButton("Show Clients");
        logoutButton = new JButton("Logout");

        exitButton.addActionListener(this);
        getOutstandingClientsButton.addActionListener(this);
        showClientsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        frame.getContentPane().add(this.exitButton);
        frame.getContentPane().add(this.getOutstandingClientsButton);
        frame.getContentPane().add(this.showClientsButton);
        frame.getContentPane().add(this.logoutButton);

        frame.setVisible(true);
        frame.paint(frame.getGraphics());
        frame.toFront();
        frame.requestFocus();

    }
}