package server;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import ServerGUI.serverController;
import common.Order;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ProtoServer extends AbstractServer {
    private DBController dbController;
    private serverController controller;
    
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
    
    private String createResponseForClient(String message) {
    	String[] words = message.split("\\s+");
    	if ("inserted".equals(words[0]))
    		return "The order sent successfully";
    	else if ("Duplicate".equals(words[0]))
    		return "The order already exists";
    	else if("updated".equals(words[0]))
    		return "The order updated successfully";
    	else if("nothing".equals(words[0]))
    		return "Nothing has changed";
    	else
    		return "Something went wrong";
    }
    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        String result;
        if (msg instanceof String[]) {
            String[] message = (String[]) msg;//WE NEED TO DELETE THIS U IDIOTS
            //Handle the initial connection message
            controller.displayClientDetails((String[])msg);
        }
        else if (msg instanceof Object[]) {
            Object[] message = (Object[]) msg;
            //if got order to insert to DB
            if ("insertOrder".equals(message[0])) {
                Order order = (Order) message[1];
                result = dbController.insertOrder(order.getOrderNumber(), order.getNameOfRestaurant(), order.getTotalPrice(), 
                                          order.getOrderListNumber(), order.getOrderAddress());
                try {
                    client.sendToClient(createResponseForClient(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //if we want to update an order
            else if ("updateOrder".equals(message[0].toString())) {
                int orderNum = (int) message[1];
                String toChange = message[2].toString();
                if ("Order_address".equals(toChange)){
                	result = dbController.updateOrder(orderNum, toChange, (String)message[3]);
                }
                else
                	result = dbController.updateOrder(orderNum, toChange, (double)message[3]);
                try {
                    client.sendToClient(createResponseForClient(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Received unknown message type from client: " + msg);
            }
        } else if (msg instanceof String) {
        	//if want to view
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
        controller.displayClientDetails((new String[]{"Client disconnected: "+client}));
        System.out.println("Client disconnected: " + client);
    }

    @Override
    protected void serverStarted() {
    	controller.updateStatus("Server listening for connections on port "+ getPort());
    }

    @Override
    protected void serverStopped() {
    	controller.updateStatus("Server has stopped listening for connections.");
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
    
    public void setController(serverController controller) {
        this.controller = controller;
     }

    public static void main(String[] args) {
        int port = 8080;

        ProtoServer sv = new ProtoServer(port);
        try {
            sv.listen();
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }   
    }
     
}
