import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private String date;
    private String description;
    private double total;
    private static final long serialVersionUID = 1L;

    public Transaction(String description, double cost) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        this.date = dtf.format(dateTime);
        this.description = description;
        this.total = cost;
    }

    public String toString() {

        return date + "    " + description + "      " + total;

    }

}