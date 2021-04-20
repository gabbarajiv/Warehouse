import java.util.*;
import java.io.*;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList clients = new LinkedList();
    private static ClientList clientList;

    private ClientList() {

    }

    public static ClientList instance() {
        if (clientList == null) {
            return clientList = new ClientList();
        } else {
            return clientList;
        }
    }

    public Client searchClient(String clientId) {
        for (Iterator iterator = clients.iterator(); iterator.hasNext();) {
            Client client = (Client) iterator.next();
            if (client.getId().equals(clientId)) {
                return client;
            }
        }
        return null;
    }

    public Iterator getClients() {
        return clients.iterator();
    }

    public Iterator getClientsOutstandingB() {
        List outstandingClients = new LinkedList<>();
        Iterator iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = (Client) iterator.next();
            if (client.getClientsBalance() < 0) {
                outstandingClients.add(client);
            }
        }
        return outstandingClients.iterator();
    }

    public boolean addClient(Client client) {
        clients.add(client);
        return true;
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(clientList);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void readObject(java.io.ObjectInputStream input) {
        try {
            if (clientList != null) {
                return;
            } else {
                input.defaultReadObject();
                if (clientList == null) {
                    clientList = (ClientList) input.readObject();
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
        return clients.toString();
    }

}
