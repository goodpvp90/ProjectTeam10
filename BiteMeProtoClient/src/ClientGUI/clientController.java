package ClientGUI;

import client.ProtoClient;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class clientController {
    private ProtoClient client;

    @FXML
    private VBox ordersContainer;

    @FXML
    private ListView<String> ordersListView;

    @FXML
    private Text welcomeText;

    public clientController() {
        try {
            client = new ProtoClient("localhost", ProtoClient.DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize client: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        // This ensures that the GUI controller is set when the FXML is loaded.
        if (client != null) {
            client.setGuiController(this);
            // Update the welcome text with the client's host name
            welcomeText.setText("Hello " + client.getHost() + "! Please choose an action:");
        } else {
            System.out.println("Client is null in initialize method");
        }
    }

    @FXML
    private void handleViewButton() {
        try {
            client.viewOrdersFromDB();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while handling view button: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateButton() {
     //notyet
    }

    @FXML
    private void handleQuitButton() {
        client.quit();
    }

    public void displayOrders(Object[] orders) {
        for (Object order : orders) {
            if (order instanceof Object[]) {
                StringBuilder orderDetails = new StringBuilder();
                for (Object obj : (Object[]) order) {
                    orderDetails.append(obj).append("\t");
                }
                ordersListView.getItems().add(orderDetails.toString());
            }
        }
    }
}
