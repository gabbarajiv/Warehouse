import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Warehouse warehouse;
    private ClientList clientList;
    private ProductList productList;
    private SupplierList supplierList;

    private Warehouse() {
        clientList = ClientList.instance();
        productList = ProductList.instance();
        supplierList = SupplierList.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            return warehouse = new Warehouse();
        } else {
            return warehouse;
        }
    }

    public Client addClient(String name, String address, String phone) {
        Client client = new Client(name, address, phone);
        if (clientList.addClient(client)) {
            return client;
        } else
            return null;
    }

    public Product addProduct(String name, float price, int quantity) {
        Product product = new Product(name, price, quantity);
        if (productList.addProduct(product)) {
            return product;
        } else
            return null;
    }

    public Supplier addSupplier(String name, String address, String phone) {
        Supplier supplier = new Supplier(name, address, phone);
        if (supplierList.addSupplier(supplier)) {
            return supplier;
        } else
            return null;
    }

    public ShipmentRecord addShipmentRecord(String productID, String supplierID, float price) {
        Product product = productList.search(productID);
        Supplier supplier = supplierList.search(supplierID);
        if (supplier != null && product != null) {
            ShipmentRecord shipmentRecord = new ShipmentRecord(product, supplier, price);
            supplier.addShipmentRecord(shipmentRecord);
            product.addShipmentRecord(shipmentRecord);
            return shipmentRecord;
        } else
            return null;
    }

    public Client changeQuantity(String clientId, String productId, int quantity) {
        Client client = clientList.searchClient(clientId);
        Cart newcart = updateCart(clientId, productId, quantity);

        client.setcart(newcart);

        return client;
    }

    public Cart updateCart(String clientId, String prodId, int quantity) {
        Client client = clientList.searchClient(clientId);
        Product product = productList.search(prodId);
        if (product != null && client != null) {
            if (client.updateCart(product, quantity)) { //
                Cart cart = client.getCart();
                return cart;
            }
            return null;
        } else {
            return null;
        }
    }

    public void clearWaitlist(String prodId) {
        Product prod = productList.search(prodId);
        Iterator waitlist = prod.getWaitlistItems();
        WaitList temp;
        while (waitlist.hasNext()) {
            temp = (WaitList) waitlist.next();
            temp.getClient().clearWaitlist();
        }
        prod.clearWaitlist();
    }

    public Iterator getClients() {
        return clientList.getClients();
    }

    public Iterator getProducts() {
        return productList.getProducts();
    }

    public Iterator getSuppliers() {
        return supplierList.getSuppliers();
    }

    public Iterator getOutstandingClients() {
        return clientList.getClientsOutstandingB();
    }

    public Cart addToCart(String clientId, String productId, int quantity) {
        Client client = clientList.searchClient(clientId);
        Product product = productList.search(productId);
        if (product != null && client != null) {
            if (client.addToCart(product, quantity)) {
                Cart cart = client.getCart();
                return cart;
            }
            return null;
        } else {
            return null;
        }
    }

    public Product changePrice(String id, String price) {

        Product product = productList.search(id);

        product.setPrice(price);
        return product;

    }

    public Client changeAddress(String id, String address) {

        Client client = clientList.searchClient(id);
        client.setAddress(address);
        return client;

    }

    public Product searchGetProduct(String productId) {
        return productList.search(productId);
    }

    public Product setProductQuantity(String pId, int qty) {
        Product prod = productList.search(pId);
        prod.setQuantity(qty);
        return prod;
    }

    public Iterator getShipmentRecord(String productId) {
        Product prod = productList.search(productId);
        return prod.getShipmentRecords();
    }

    public Iterator getProductRecord(String supplierId) {
        Supplier sup = supplierList.search(supplierId);
        return sup.getShipmentRecords();
    }

    public Supplier searchGetSupplier(String sId) {
        return supplierList.search(sId);
    }

    public Cart getCart(String id) {
        Client client = clientList.searchClient(id);
        return client.getCart();
    }

    public Iterator getWaitlistClient(String clientId) {
        Client client = clientList.searchClient(clientId);
        if (client != null) {
            return client.getWaitlistProducts();
        } else {
            return null;
        }
    }

    public Client searchClient(String clientID) {
        return clientList.searchClient(clientID);
    }

    public Client getClientInfo(String clientId) {
        return clientList.searchClient(clientId);
    }

    public Iterator getWaitlistProd(String productId) {
        Product product = productList.search(productId);
        if (product != null) {
            return product.getWaitlistItems();
        } else {
            return null;
        }

    }

    public String processOrder(String id) {
        Client client = clientList.searchClient(id);
        String invoice = client.makeOrder();
        return invoice;
    }

    public Iterator getClientTransactions(String id) {
        Client client = clientList.searchClient(id);
        if (client != null) {
            return client.getTransactions();
        } else
            return null;
    }

    public String fillClientWaitlist(WaitList item, int fillQuantity) {
        String invoice;
        WaitList temp;
        Client client = item.getClient();

        invoice = client.processCharge(item.getProduct(), fillQuantity);
        return invoice;
    }

    public static Warehouse retrieve() {
        try (FileInputStream file = new FileInputStream("WarehouseData");
                ObjectInputStream input = new ObjectInputStream(file);) {

            input.readObject();
            ClientIdServer.retrieve(input);
            SupplierIdServer.retrieve(input);
            ProductIdServer.retrieve(input);

            return warehouse;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    private static void readObject(java.io.ObjectInputStream input) {
        try {
            input.defaultReadObject();
            if (warehouse == null) {
                warehouse = (Warehouse) input.readObject();
            } else {
                input.readObject();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean save() {
        try (FileOutputStream file = new FileOutputStream("WarehouseData");
                ObjectOutputStream output = new ObjectOutputStream(file)) {
            output.writeObject(warehouse);
            output.writeObject(ProductIdServer.instance());
            output.writeObject(ClientIdServer.instance());
            output.writeObject(SupplierIdServer.instance());
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;

        }

    }

    public String toString() {
        return (productList + "\n" + clientList + "\n" + supplierList);
    }
}
