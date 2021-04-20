import java.util.*;
import java.io.*;

public class SupplierList implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList suppliers = new LinkedList();
    private static SupplierList supplierList;

    private SupplierList() {
    }

    public static SupplierList instance() {
        if (supplierList == null) {
            return (supplierList = new SupplierList());
        } else {
            return supplierList;
        }
    }

    public boolean addSupplier(Supplier supplier) {
        suppliers.add(supplier);
        return true;
    }

    public Iterator getSuppliers() {
        return suppliers.iterator();
    }

    public Supplier search(String supplierID) {
        Iterator supplierlist = getSuppliers();
        while (supplierlist.hasNext()) {
            Supplier temp = (Supplier) supplierlist.next();
            if (temp.getId().equals(supplierID)) {
                return temp;
            }
        }
        return null;
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(supplierList);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (supplierList != null) {
                return;
            } else {
                input.defaultReadObject();
                if (supplierList == null) {
                    supplierList = (SupplierList) input.readObject();
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

    public String toString() {
        return suppliers.toString();
    }
}
