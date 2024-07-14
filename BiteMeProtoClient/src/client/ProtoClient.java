package client;
import ocsf.client.*;
import java.io.*;
import java.net.InetAddress;
import common.Order;
import ClientGUI.clientController;

public class ProtoClient extends AbstractClient {
    final public static int DEFAULT_PORT = 8080;
    private clientController clientController;//For ClientGUI functionality

    public ProtoClient(String host, int port) throws IOException {
        super(host, port);
        openConnection();
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientHostName = InetAddress.getLocalHost().getHostName();
        sendToServer(new String[] { clientIP, clientHostName, "start" });
    }
    
    //Constructed a controller for this clientGUI
    public void setGuiController(clientController clientController) {
        this.clientController = clientController;
    }
    
    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof Object[]) {
            Object[] orders = (Object[]) msg;
            if (clientController != null) {
                clientController.displayOrders(orders);
            }
        } else {
            //Handles non-array messages in clientController, for updating the top label
            if (clientController != null) {
                clientController.updateWelcomeText("Message from server: " + msg);
            }
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
            if (clientController != null) {
                clientController.updateWelcomeText("Failed to send message to server: " + e.getMessage());
            }
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
    
    //For testing functionality outside clientGUI
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
        Order order2 = new Order(123, "MC", 25.99, 1, "4567 Elm St");
        client.sendInsertOrderRequest(order2);
    }
    
    
    
}