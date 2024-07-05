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
            sendToServer(msg);
        } catch (Exception e) {
            System.out.println("Failed to send message to server: " + e.getMessage());
        }
    }

    public void sendUpdateOrderRequest(Order order, String valueToChange) {
        Object[] arr = new Object[2];
        arr[0] = order;
        arr[1] = valueToChange;
        sendMessageToServer(arr);
    }

    public void sendInsertOrderRequest(Order order) {
        sendMessageToServer(new Object[] { "insertOrder", order });
    }

    public void viewOrdersFromDB() {
        sendMessageToServer("view");
    }

    public static void main(String[] args) throws IOException {
        ProtoClient client = new ProtoClient("localhost", DEFAULT_PORT);
        try {
            client.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Order order = new Order(777, "Domino's", 25.99, 1, "456 Elm St");
        client.sendInsertOrderRequest(order);
        client.viewOrdersFromDB();
        Order order2 = new Order(777, "Domino's", 104, 1, "456 Elm St");
        client.sendUpdateOrderRequest(order2, "total_price");
    }
}
