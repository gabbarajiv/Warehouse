import java.util.*;
import java.io.*;

public class Supplier implements Serializable {

    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierId;

    private static final long serialVersionUID = 1L;
    private static final String SUPPLIER_STRING = "S";
    private LinkedList SupplierList = new LinkedList<>();
    private List<ShipmentRecord> record = new LinkedList<ShipmentRecord>();

    public Supplier(String name, String address, String phone) {
        this.supplierName = name;
        this.supplierAddress = address;
        this.supplierPhone = phone;
        supplierId = SUPPLIER_STRING + (SupplierIdServer.instance()).getId();
    }

    public String getName() {
        return supplierName;
    }

    public void setName(String newName) {
        supplierName = newName;
    }

    public String getAddress() {
        return supplierAddress;
    }

    public void setAddress(String newAddress) {
        supplierAddress = newAddress;
    }

    public String getPhone() {
        return supplierPhone;
    }

    public void setPhone(String newPhone) {
        supplierPhone = newPhone;
    }

    public String getId() {
        return supplierId;
    }

    public boolean equals(String id) {
        return this.supplierId.equals(id);
    }

    public boolean addShipmentRecord(ShipmentRecord shipmentRecord) {
        return record.add(shipmentRecord);
    }

    public Iterator<ShipmentRecord> getShipmentRecords() {
        return record.iterator();

    }

    public String toString() {
        return "SupplierId: " + supplierId + "       SupplierName:  " + supplierName + "       SupplierAddress:  " + supplierAddress
                + "      SupplierPhone:  " + supplierPhone;
    }

}
