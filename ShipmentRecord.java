//this class connects products and supplier

import java.io.*;


public class ShipmentRecord implements Serializable {
    private Product product;
    private Supplier supplier;
    private float price;

    private static final long serialVersionUID = 1L;

    public ShipmentRecord(Product product, Supplier supplier, float d) {
        this.product = product;
        this.supplier = supplier;
        this.price = d;
    }

    public Product getProduct() {
        return product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public double getPrice() {
        return price;
    }

    public String toString() {
        return "ProductID:   " + product.getId() + "        SupplierID:   " + supplier.getId() + "          Price:  "
                + price;
    }
}
