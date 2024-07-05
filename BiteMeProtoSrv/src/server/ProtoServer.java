package server;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import common.Order;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ProtoServer extends AbstractServer {
    private DBController dbController;

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

        if (msg instanceof String[]) {
            String[] message = (String[]) msg;
            // Handle the initial connection message
            System.out.println("Received start/end message: " + String.join(", ", message));
        } else if (msg instanceof Object[]) {
            Object[] message = (Object[]) msg;

            if ("insertOrder".equals(message[0])) {
                Order order = (Order) message[1];
                dbController.insertOrder(order.getOrderNumber(), order.getNameOfRestaurant(), order.getTotalPrice(), 
                                          order.getOrderListNumber(), order.getOrderAddress());
                try {
                    client.sendToClient("Order inserted successfully: " + order.getOrderNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (message.length > 1 && message[0] instanceof Order) {
                Order order = (Order) message[0];
                String toChange = (String) message[1];
                dbController.updateOrder(order.getOrderNumber(), toChange, order.getTotalPrice());

                try {
                    client.sendToClient("Order updated successfully: " + order.getOrderNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Received unknown message type from client: " + msg);
            }
        } else if (msg instanceof String) {
            if ("view".equals(msg)) {
                List<Object[]> orders = dbController.showOrders();
                try {
                    client.sendToClient(orders.toArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Received unknown message from client: " + msg);
            }
        } else {
            System.out.println("Received unknown message from client: " + msg);
        }
    }

    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected: " + client);
    }

    @Override
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    @Override
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
        dbController.closeConnection();
    }

    public void stopServer() {
        try {
            stopListening();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8080;

        ProtoServer sv = new ProtoServer(port);
        try {
            sv.listen();
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }

        // Example to stop the server after some time (for testing purposes)
        /*try {
            Thread.sleep(30000); // Let the server run for 30 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sv.stopServer();*/
    }
}
