package ClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import client.ProtoClient;

public class clientController {
    private ProtoClient client;
    private boolean updating = false;
    private Consumer<List<String>> displayOrdersCallback;
    @FXML
    private VBox ordersContainer;
    @FXML
    private ListView<String> ordersListView;
    @FXML
    private Text welcomeText;
    @FXML
    private TextField orderNumberField;
    @FXML
    private ComboBox<String> fieldComboBox;
    @FXML
    private TextField newValueField;
    @FXML
    private Button updateButton;
    
    //Constructs the controller with the name & port
    public clientController() {
        try {
            client = new ProtoClient("localhost", ProtoClient.DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize client: " + e.getMessage());
        }
    }

    //initializes the controller and sets up the default view in the clientUI
    @FXML
    private void initialize() {
        if (client != null) {
            client.setGuiController(this);
            // Update the welcome text with the client's host name
            welcomeText.setText("Hello " + client.getHost() + "! Please choose an action:");
        } else {
            System.out.println("Client is null in initialize method");
        }
        fieldComboBox.setItems(FXCollections.observableArrayList("Total_price", "Order_address"));
    }

    //Handles the VIEW button logics. lets you interact with it only if updating is false (means that we didn't click it earlier)
    @FXML
    private void handleViewButton() {
        if (!updating) {
            try {
                client.viewOrdersFromDB();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error while handling view button: " + e.getMessage());
            }
        }
    }

    //Handles the pressing of the VIEW button. Updating is TRUE, the button in disabled and the lower part of the UI is visible to make changes
    @FXML
    private void handleUpdateButton() {
        updating = true;
        updateButton.setDisable(true);
        ordersContainer.setVisible(true);
    }

    //Handles the confirmation part in the UPDATE part. checks for incorrect inputs and turns on the UPDATE button in case of a legal information update
    @FXML
    private void handleConfirmButton() {
        try {
            int orderNum = Integer.parseInt(orderNumberField.getText());
            String field = fieldComboBox.getValue();
            String newValue = newValueField.getText();
            //Checks for empty values
            if (field == null || newValue.isEmpty()) {
            	welcomeText.setText("Please fill in all fields.");
                return;
            }
            //Checks what field was asked to be updated and handle invalid fields
            Object newVal;
            if ("Total_price".equals(field)) {
                newVal = Double.parseDouble(newValue);
            } else if ("Order_address".equals(field)) {
                newVal = newValue;
            } else {
            	welcomeText.setText("Invalid field selection.");
                return;
            }
            //Uses the client's methods to complete a legal update process
            client.sendUpdateOrderRequest(orderNum, field, newVal);
            //resets the clientUI to default and its related values
            orderNumberField.clear();
            fieldComboBox.setValue(null);
            newValueField.clear();
            ordersContainer.setVisible(false);
            updateButton.setDisable(false);
            updating = false;
            //In case of a wrong value input
        } catch (NumberFormatException e) {
        	welcomeText.setText("Invalid input: " + e.getMessage());
            //In case of an error during update process
        } catch (Exception e) {
            System.out.println("Error during update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Closes the program on the client side
    @FXML
    private void handleQuitButton() {
        client.quit();
    }

    //Handles continuous use of the VIEW button
    public void setDisplayOrdersCallback(Consumer<List<String>> callback) {
        this.displayOrdersCallback = callback;
    }

    //Handles the VIEW process 
    public void displayOrders(Object[] orders) {
        Platform.runLater(() -> {
            ordersListView.getItems().clear();
            for (Object order : orders) {
                if (order instanceof Object[]) {
                    StringBuilder orderDetails = new StringBuilder();
                    for (Object obj : (Object[]) order) {
                        orderDetails.append(obj).append("\t");
                    }
                    ordersListView.getItems().add(orderDetails.toString());
                }
            }
        });
    }

    //Method to update The top lable (welcome text)
    public void updateWelcomeText(String message) {
        Platform.runLater(() -> {
            welcomeText.setText(message);
        });
    }
}
