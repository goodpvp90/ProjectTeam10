package client;

import ocsf.client.*;
import java.io.*;
import java.net.InetAddress;

public class ProtoClient extends AbstractClient {
    final public static int DEFAULT_PORT = 8080;

    public ProtoClient(String host, int port) throws IOException {
        super(host, port);
        openConnection();
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientHostName = InetAddress.getLocalHost().getHostName();
        sendToServer(new String[] { clientIP, clientHostName, "start" });
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        // Print the message from the server to the console
        System.out.println("Message from server: " + msg.toString());
    }

    /***
     * this function quits the activity of the client and close its connection to the server
     * and signal the server that the client who was active stopped using the system .
     */
    public void quit() {
        try {
            String clientIP = InetAddress.getLocalHost().getHostAddress();
            String clientHostName = InetAddress.getLocalHost().getHostName();
            sendToServer(new String[] { clientIP, clientHostName, "end" });
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void sendMessageToServer(Object msg) {
        try {
            // Send the message to the server
            sendToServer(msg);
        } catch (Exception e) {
            System.out.println("Failed to send message to server: " + e.getMessage());
        }
    }

    public void sendUpdateOrderRequest(int orderNumber, String restaurantName, double totalPrice, int orderListNumber, String orderAddress) {
        Object[] updateRequest = { "updateOrder", orderNumber, restaurantName, totalPrice, orderListNumber, orderAddress };
        sendMessageToServer(updateRequest);
    }
    
    public void sendInsertOrderRequest(int orderNumber, String restaurantName, double totalPrice, int orderListNumber, String orderAddress) {
        Object[] insertRequest = { "insertOrder", orderNumber, restaurantName, totalPrice, orderListNumber, orderAddress };
        sendMessageToServer(insertRequest);
    }

    public static void main(String[] args) throws IOException {
        // Example usage
        ProtoClient client = new ProtoClient("localhost", DEFAULT_PORT);
        try {
            client.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.sendMessageToServer("Hello, server!");

        // Example update order request
        client.sendInsertOrderRequest(5, "Domino's", 25.99, 2, "456 Elm St");

        //client.sendUpdateOrderRequest(1, "Pizza Hut", 29.99, 123, "123 Main St");
    }
}
