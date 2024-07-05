package client;

import ocsf.client.*;
import java.io.*;
import java.net.InetAddress;

import common.Order;

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
        if (msg instanceof Object[]) {
            Object[] orders = (Object[]) msg;
            for (Object order : orders) {
                Object[] orderDetails = (Object[]) order;
                System.out.println("Order Number: " + orderDetails[0] + ", Restaurant: " + orderDetails[1] +
                        ", Total Price: " + orderDetails[2] + ", Order List Number: " + orderDetails[3] +
                        ", Order Address: " + orderDetails[4]);
            }
        } else {
            System.out.println("Message from server: " + msg);
        }
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

    public void sendUpdateOrderRequest(Order order, String valueToChange) {
    	Object[] arr = new Object[2];
    	arr[0] = order;
    	arr[1] = valueToChange;
        sendMessageToServer(order);
    }
    
    public void sendInsertOrderRequest(Order order) {
        sendMessageToServer(order);
    }
    
    
    public void viewOrdersFromDB() {
    	sendMessageToServer("view");
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
//    	Order order = new Order("Domino's", "456 Elm St", 1, 1623, 25.99);
//        client.sendInsertOrderRequest(order);
//        client.viewOrdersFromDB();
//    	Order orderupdate = new Order("Domino's", "456 Elm St", 5, 1343, 78);
//        client.sendUpdateOrderRequest(orderupdate,"Total_price");
    }
}
