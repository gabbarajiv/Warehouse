import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.text.DateFormat.getDateInstance;

public class ManagerState extends WarehouseState implements ActionListener {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private WarehouseContext context;
  private static Warehouse warehouse;
  private static ManagerState instance;
  private Security security = new Security();
  private JFrame frame;

  private AbstractButton exitButton;
  private AbstractButton addProductButton;
  private AbstractButton addSupplierButton;
  private AbstractButton listSupplierButton;
  private AbstractButton listSupplierProductButton;
  private AbstractButton listProductSupplierButton;
  private AbstractButton addSupplierProductButton;
  private AbstractButton modifyPriceButton;
  private AbstractButton becomeSalesClerkButton;
  private AbstractButton logoutButton;
  private AbstractButton saveButton;

  private ManagerState() {
    super();
    warehouse = Warehouse.instance();
  }

  public static ManagerState instance() {
    if (instance == null) {
      instance = new ManagerState();
    }
    return instance;
  }

  public void addPRODUCT() {
    String name = JOptionPane.showInputDialog(frame, "Please enter the Product name");
    float price = Float.parseFloat(JOptionPane.showInputDialog(frame, "Please enter the price"));
    int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Please enter the quantity"));
    Product result = warehouse.addProduct(name, price, quantity);
    if (result == null) {
      JOptionPane.showInputDialog(frame, "Please enter the quantity");
    } else {
      JOptionPane.showMessageDialog(frame, result);
    }
  }

  public void addSUPPLIER() {
    String name = JOptionPane.showInputDialog(frame, "Please enter the Supplier name");
    String address = JOptionPane.showInputDialog(frame, "Please enter the Supplier address");
    String phone = JOptionPane.showInputDialog(frame, "Please enter the Supplier phone");
    Supplier result = warehouse.addSupplier(name, address, phone);
    if (result == null) {
      JOptionPane.showInputDialog(frame, "Couldn't add a supplier");
    } else {
      JOptionPane.showMessageDialog(frame, result);
    }
  }

  public void listSUPPLIERS() {
    Iterator allSuppliers = warehouse.getSuppliers();
    JOptionPane.showInputDialog(frame, "SupllierList");
    while (allSuppliers.hasNext()) {
      Supplier temp = (Supplier) allSuppliers.next();
      JOptionPane.showMessageDialog(frame, temp);
    }
  }

  public void listSUPPLIERSBYPRODUCT() {

    String prodId = JOptionPane.showInputDialog(frame, "Please enter the productId");

    if (warehouse.searchGetProduct(prodId) != null) {
      Iterator supplierList = warehouse.getShipmentRecord(prodId);

      while (supplierList.hasNext()) {
        ShipmentRecord shipment = (ShipmentRecord) (supplierList.next());
        JOptionPane.showMessageDialog(frame, shipment);
      }

    } else {
      JOptionPane.showMessageDialog(frame, "Product not found");
    }
  }

  public void listPRODUCTSBYSUPPLIER() {
    JOptionPane.showMessageDialog(frame, "Show product list for supplier.");
    String supplierId = JOptionPane.showInputDialog(frame, "Enter Supplier Id");

    if (warehouse.searchGetSupplier(supplierId) != null) {
      Iterator productList = warehouse.getProductRecord(supplierId);

      while (productList.hasNext()) {
        ShipmentRecord shipment = (ShipmentRecord) (productList.next());
        JOptionPane.showMessageDialog(frame, shipment);
      }

    } else {
      JOptionPane.showMessageDialog(frame, "Supplier not found");
    }
  }

  public void addSUPPLIERTOPRODUCT() {
    String prodId = JOptionPane.showInputDialog(frame, "Enter the product Id");
    if (warehouse.searchGetProduct(prodId) != null) {
      Iterator supplierList = warehouse.getShipmentRecord(prodId);

      while (supplierList.hasNext()) {
        ShipmentRecord shipment = (ShipmentRecord) (supplierList.next());
        JOptionPane.showMessageDialog(frame, shipment);
      }

      String supplierId = JOptionPane.showInputDialog(frame, "Enter te supplier id");
      if (warehouse.searchGetSupplier(supplierId) != null) {
        Iterator productList = warehouse.getProductRecord(supplierId);

        while (productList.hasNext()) {
          ShipmentRecord shipment = (ShipmentRecord) (productList.next());
          JOptionPane.showMessageDialog(frame, shipment);
        }

      } else {
        JOptionPane.showMessageDialog(frame, "Supplier not found");
      }

    } else {
      JOptionPane.showMessageDialog(frame, "Product not found");
    }
  }

  public void modifyPRICE() {
    String id = JOptionPane.showInputDialog(frame, "Enter the Supplier Id");
    Product result;
    String price = JOptionPane.showInputDialog(frame, "Enter the new Price");
    result = warehouse.changePrice(id, price);
    if (result == null) {
      JOptionPane.showMessageDialog(frame, "Couldnot change price for that item");
    } else {
      JOptionPane.showMessageDialog(frame, "result");
    }
  }

  public void exit() {

    (WarehouseContext.instance()).changeState(3);
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.exitButton))
      this.exit();
    else if (event.getSource().equals(this.addProductButton))
      this.addPRODUCT();
    else if (event.getSource().equals(this.addSupplierButton))
      this.addSUPPLIER();
    else if (event.getSource().equals(this.listSupplierButton))
      this.listSUPPLIERS();
    else if (event.getSource().equals(this.listSupplierProductButton))
      this.listPRODUCTSBYSUPPLIER();
    else if (event.getSource().equals(this.listProductSupplierButton))
      this.listSUPPLIERSBYPRODUCT();
    else if (event.getSource().equals(this.addSupplierProductButton))
      this.addSUPPLIERTOPRODUCT();
    else if (event.getSource().equals(this.modifyPriceButton))
      this.modifyPRICE();
    else if (event.getSource().equals(this.becomeSalesClerkButton))
      this.becomeClerk();
    else if (event.getSource().equals(this.logoutButton))
      this.logOUT();
    else if (event.getSource().equals(this.saveButton)) {
      this.save();
    }

  }

  public void becomeClerk() {
    String clerkID = JOptionPane.showInputDialog(frame, "Input clerk Id = clerk");
    String password = JOptionPane.showInputDialog(frame, "Input clerk pasword = password");
    (WarehouseContext.instance()).setLogin(WarehouseContext.ISCLERK);
    if (security.verifyPassword(clerkID, password, 1) == true) {
      (WarehouseContext.instance()).setUser(clerkID);
      (WarehouseContext.instance()).changeState(1);
    } else
      JOptionPane.showMessageDialog(frame, "invalid user id");
  }

  public void logOUT() {
    (WarehouseContext.instance()).changeState(3); // exit with a code 0
  }

  private void save() {
    if (Warehouse.save()) {
      JOptionPane.showMessageDialog(frame, "The warehouse has been successfully saved in the file WarehouseData \n");
    } else {
      JOptionPane.showMessageDialog(frame, "Error While saving \n");
    }
  }

  public void run() {
    frame = WarehouseContext.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    exitButton = new JButton("Exit");
    addProductButton = new JButton("Add Product");
    addSupplierButton = new JButton("Add Supplier");
    listSupplierButton = new JButton("List of all the Suppliers");
    listSupplierProductButton = new JButton("List of all the Suppliers for a product");
    listProductSupplierButton = new JButton("List of all the products for a supplier");
    addSupplierProductButton = new JButton("Add supplier for a product");
    modifyPriceButton = new JButton("Change price of a product");
    becomeSalesClerkButton = new JButton("Become a SalesClerk");
    logoutButton = new JButton("logout");
    saveButton = new JButton("save");

    exitButton.addActionListener(this);
    addProductButton.addActionListener(this);
    addSupplierButton.addActionListener(this);
    listSupplierButton.addActionListener(this);
    listSupplierProductButton.addActionListener(this);
    listProductSupplierButton.addActionListener(this);
    addSupplierProductButton.addActionListener(this);
    modifyPriceButton.addActionListener(this);
    becomeSalesClerkButton.addActionListener(this);
    logoutButton.addActionListener(this);
    saveButton.addActionListener(this);

    frame.getContentPane().add(this.exitButton);
    frame.getContentPane().add(this.addProductButton);
    frame.getContentPane().add(this.addSupplierButton);
    frame.getContentPane().add(this.listSupplierButton);
    frame.getContentPane().add(this.listSupplierProductButton);
    frame.getContentPane().add(this.listProductSupplierButton);
    frame.getContentPane().add(this.addSupplierProductButton);
    frame.getContentPane().add(this.modifyPriceButton);
    frame.getContentPane().add(this.becomeSalesClerkButton);
    frame.getContentPane().add(this.logoutButton);
    frame.getContentPane().add(this.saveButton);

    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();

  }

}
