import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginState extends WarehouseState implements ActionListener {

  private JFrame frame;
  private static LoginState instance;
  private AbstractButton clientButton;
  private AbstractButton clerkButton;
  private AbstractButton managerButton;

  private Security security = new Security();

  private LoginState() {
    super();
    // context = WarehouseContext.instance();
  }

  public static LoginState instance() {
    if (instance == null) {
      instance = new LoginState();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.clientButton)) {
      this.client();
    } else if (event.getSource().equals(this.clerkButton)) {
      this.clerk();

    } else if (event.getSource().equals(this.managerButton))
      this.manager();
  }

  public void clear() {
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());
  }

  private void clerk() {
    String clerkID = JOptionPane.showInputDialog(frame, "Please enter the clerk id");
    String password = JOptionPane.showInputDialog(frame, "Please enter the clerk password");
    if (security.verifyPassword(clerkID, password, 1)) {
      (WarehouseContext.instance()).setLogin(WarehouseContext.ISCLERK);
      clear();
      (WarehouseContext.instance()).changeState(1);
    } else
      JOptionPane.showInputDialog(frame, "Invalidd User Id");
  }

  private void client() {
    String clientID = JOptionPane.showInputDialog(frame, "Please enter the client id");
    if (Warehouse.instance().getClientInfo(clientID) != null) {
      (WarehouseContext.instance()).setLogin(WarehouseContext.ISCLIENT);
      (WarehouseContext.instance()).setUser(clientID);
      clear();
      (WarehouseContext.instance()).changeState(2);
    } else
      JOptionPane.showMessageDialog(frame, "Invalid User Id");

  }

  private void manager() {
    String managerID = JOptionPane.showInputDialog(frame, "Please enter the manager id");
    String password = JOptionPane.showInputDialog(frame, "Please enter the manager password");
    if (security.verifyPassword(managerID, password, 0) == true) {
      (WarehouseContext.instance()).setLogin(WarehouseContext.ISMANAGER);
      (WarehouseContext.instance()).changeState(WarehouseContext.MANAGER_STATE);
    } else
      JOptionPane.showInputDialog(frame, "Invalid User Id");
  }

  public void run() {

    frame = WarehouseContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    clientButton = new JButton("Client");
    clerkButton = new JButton("Clerk");
    managerButton = new JButton("Manager");

    clientButton.addActionListener(this);
    clerkButton.addActionListener(this);
    managerButton.addActionListener(this);

    frame.getContentPane().add(this.clientButton);
    frame.getContentPane().add(this.clerkButton);
    frame.getContentPane().add(this.managerButton);

    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();

  }

}