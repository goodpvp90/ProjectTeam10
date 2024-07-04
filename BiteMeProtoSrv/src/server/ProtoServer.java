package server;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import common.Order;
import ocsf.server.*;

public class ProtoServer extends AbstractServer {
    // Class variables *************************************************

    /**
     * The default port to listen on.
     */

    private DBController dbController;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public ProtoServer(int port) {
        super(port);
        dbController = new DBController();
        try {
            dbController.connect();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);
        
        if (msg instanceof Order) {
        	
        }
        
        if (msg instanceof Object[]) {
            Object[] message = (Object[]) msg;
            
            if ("updateOrder".equals(message[0])) {
                int orderNumber = (int) message[1];
                String restaurantName = (String) message[2];
                double totalPrice = (double) message[3];
                int orderListNumber = (int) message[4];
                String orderAddress = (String) message[5];
                
                dbController.updateOrder(orderNumber, restaurantName, totalPrice, orderListNumber, orderAddress);
                
                // Optionally, send a response back to the client confirming the update
                try {
                    client.sendToClient("Order updated successfully: " + orderNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else if (msg instanceof Order) {
                int orderNumber = (int) message[1];
                String restaurantName = (String) message[2];
                double totalPrice = (double) message[3];
                int orderListNumber = (int) message[4];
                String orderAddress = (String) message[5];
                
                dbController.insertOrder(orderNumber, restaurantName, totalPrice, orderListNumber, orderAddress);
                
                // Optionally, send a response back to the client confirming the insertion
                try {
                    client.sendToClient("Order inserted successfully: " + orderNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else {
                System.out.println("Received unknown message type from client: " + msg);
            }
        } else if (msg instanceof String)
        	if (msg.equals("view"))
        {
        		List<Object[]> orders = dbController.showOrders();
                try {
                    client.sendToClient(orders.toArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
            System.out.println("Received unknown message from client: " + msg);
        }
    }

    /**
     * This method overrides the one in the superclass. Called when the server starts
     * listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass. Called when the server stops
     * listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
        dbController.closeConnection();
    }

    // Class methods ***************************************************

    /**
     * This method is responsible for the creation of the server instance (there is no
     * UI in this phase).
     *
     * @param args[0] The port number to listen on. Defaults to 5555 if no argument is
     *                entered.
     */
    public static void main(String[] args) {
        int port = 8080; // Port to listen on

        ProtoServer sv = new ProtoServer(port);
        try {
            sv.listen(); // Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}
