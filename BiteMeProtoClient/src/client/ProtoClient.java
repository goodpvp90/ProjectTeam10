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
                //////////////////////////need to send to controller
                System.out.println("Order Number: " + orderDetails[0] + ", Restaurant: " + orderDetails[1] +
                        ", Total Price: " + orderDetails[2] + ", Order List Number: " + orderDetails[3] +
                        ", Order Address: " + orderDetails[4]);
            }
        } else {
        	//send (String)msg to GUIcontroller//
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

    public void sendUpdateOrderRequest(int orderNum, String colToChange, Object newVal) {
        Object[] arr = new Object[4];
        arr[0] = "updateOrder";
        arr[1] = orderNum;
        arr[2] = colToChange;
        arr[3] = newVal;
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
        client.sendUpdateOrderRequest(777, "total_price", 13.44);
        client.viewOrdersFromDB();
        client.sendUpdateOrderRequest(777, "order_address", "hazamir");
        client.viewOrdersFromDB();
        
       
        
    }
}
