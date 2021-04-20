import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class WarehouseContext {

    private int currentState;
    private static Warehouse warehouse;
    private static WarehouseContext context;
    private int currentUser;
    private String userID = "id";
    static private String ManagerID;
    static private String ClerkID;
    static private String ClientID;

    private static JFrame WarehouseFrame;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static final int ISMANAGER = 0;
    public static final int ISCLERK = 1;
    public static final int ISCLIENT = 2;
    public static final int ISCART = 3;
    public static final int ISQUERY = 4;
    public static final int MANAGER_STATE = 0;
    public static final int CLERK_STATE = 1;
    public static final int CLIENT_STATE = 2;
    public static final int LOGIN_STATE = 3;
    public static final int SHOPPING_CART_STATE = 4;
    public static final int QUERY_STATE = 5;
    private static WarehouseState[] states;
    private int[][] nextState;

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
    }

    private static void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                JOptionPane.showMessageDialog(WarehouseFrame,
                        " The Warehouse has been successfully retrieved from the file WarehouseData \n");
                warehouse = tempWarehouse;
            } else {
                JOptionPane.showMessageDialog(WarehouseFrame, "File doesnt exist; creating new Warehouse");
                warehouse = Warehouse.instance();
            }
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    private WarehouseContext() { // constructor
        System.out.println("In Warehousecontext constructor");
        if (yesOrNo("Look for saved data and  use it?")) {
            retrieve();
        } else {
            warehouse = Warehouse.instance();
        }
        // set up the FSM and transition table;
        states = new WarehouseState[6];
        states[0] = ManagerState.instance();
        states[1] = ClerkState.instance();
        states[2] = ClientState.instance();
        states[3] = LoginState.instance();
        states[4] = CartState.instance();
        states[5] = QueryState.instance();

        nextState = new int[6][6];
        nextState[0][0] = 0;
        nextState[0][1] = 1;
        nextState[0][2] = 2;
        nextState[0][3] = 3;
        nextState[0][4] = -1;
        nextState[0][5] = -1;

        nextState[1][0] = 3;
        nextState[1][1] = 1;
        nextState[1][2] = 2;
        nextState[1][3] = 3;
        nextState[1][4] = -1;
        nextState[1][5] = 5;

        nextState[2][0] = -2;
        nextState[2][1] = -2;
        nextState[2][2] = 2;
        nextState[2][3] = 3;
        nextState[2][4] = 4;
        nextState[2][5] = -1;

        nextState[3][0] = 0;
        nextState[3][1] = 1;
        nextState[3][2] = 2;
        nextState[3][3] = 3;
        nextState[3][4] = -1;
        nextState[3][5] = -1;

        nextState[4][0] = 4;
        nextState[4][1] = -1;
        nextState[4][2] = 2;
        nextState[4][3] = 3;
        nextState[4][4] = 4;
        nextState[4][5] = -1;

        nextState[5][0] = 3;
        nextState[5][1] = 1;
        nextState[5][2] = -1;
        nextState[5][3] = 3;
        nextState[5][4] = -1;
        nextState[5][5] = 5;

        currentState = 3;
        WarehouseFrame = new JFrame("Warehousen GUI");

        WarehouseFrame.setSize(1000, 1000);
        WarehouseFrame.setLocation(500, 500);
    }

    public void changeState(int transition) {

        currentState = nextState[currentState][transition];
        if (currentState == -2) {
            JOptionPane.showInputDialog(WarehouseFrame, "Error");
            terminate();
        }
        if (currentState == -1)
            terminate();

        states[currentState].run();
    }

    public void setLogin(int code) {
        currentUser = code;
    }

    public void setUser(String uID) {
        userID = uID;
    }

    public int getLogin() {
        return currentUser;
    }

    public String getUser() {
        return userID;
    }

    public static void setManager(String uID) {
        ManagerID = uID;
    }

    public static void setClerk(String uID) {
        ClerkID = uID;
    }

    public static void setClient(String uID) {
        ClientID = uID;
    }

    public static String getManager() {
        return ManagerID;
    }

    public static String getClient() {
        return ClientID;
    }

    public static String getClerk() {
        return ClerkID;
    }

    public JFrame getFrame() {
        return WarehouseFrame;
    }

    private void terminate() {
        if (yesOrNo("Save data?")) {
            if (Warehouse.save()) {
                System.out.println(" The library has been successfully saved in the file LibraryData \n");
            } else {
                System.out.println(" There has been an error in saving \n");
            }
        }
        System.out.println(" Goodbye \n ");
        System.exit(0);
    }

    public static WarehouseContext instance() {
        if (context == null) {
            // System.out.println("calling constructor");
            context = new WarehouseContext();
        }
        return context;
    }

    public void process() {
        states[currentState].run();
    }

    public static void main(String[] args) {
        WarehouseContext.instance().process();

    }

}
