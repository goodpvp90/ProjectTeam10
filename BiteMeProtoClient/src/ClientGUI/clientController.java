package ClientGUI;

import client.ProtoClient;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class clientController {
    private ProtoClient client;

    @FXML
    private VBox ordersContainer;

    @FXML
    private ListView<String> ordersListView;
    
    public clientController() {
        try {
            client = new ProtoClient("localhost", ProtoClient.DEFAULT_PORT);
            client.setGuiController(this);
            System.out.println("Client initialized and controller set");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize client: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        if (client != null) {
            client.setGuiController(this);
            System.out.println("Controller set in initialize method");
        } else {
            System.out.println("Client is null in initialize method");
        }
    }

    @FXML
    private void handleViewButton() {
        try {
            System.out.println("View button clicked, requesting orders from DB");
            client.viewOrdersFromDB();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while handling view button: " + e.getMessage());
        }
    }

    public void displayOrders(Object[] orders) {
        System.out.println("Displaying orders");
        if (orders == null) {
            System.out.println("Received null orders");
            return;
        }

        // Clear previous orders
        if (ordersContainer != null) {
            ordersContainer.getChildren().clear();
            for (Object order : orders) {
                try {
                    Object[] orderDetails = (Object[]) order;
                    Text orderText = new Text("Order Number: " + orderDetails[0] + ", Restaurant: " + orderDetails[1] +
                            ", Total Price: " + orderDetails[2] + ", Order List Number: " + orderDetails[3] +
                            ", Order Address: " + orderDetails[4]);
                    ordersContainer.getChildren().add(orderText);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error while displaying orders: " + e.getMessage());
                }
            }
        }

        // Using ListView
        if (ordersListView != null) {
            ObservableList<String> ordersList = FXCollections.observableArrayList();
            for (Object order : orders) {
                try {
                    Object[] orderDetails = (Object[]) order;
                    String orderText = "Order Number: " + orderDetails[0] + ", Restaurant: " + orderDetails[1] +
                            ", Total Price: " + orderDetails[2] + ", Order List Number: " + orderDetails[3] +
                            ", Order Address: " + orderDetails[4];
                    ordersList.add(orderText);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error while displaying orders: " + e.getMessage());
                }
            }
            ordersListView.setItems(ordersList);
        }
    }
}
