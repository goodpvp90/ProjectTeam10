package ServerGUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ocsf.server.ConnectionToClient;
import server.ProtoServer;

public class serverController {

    @FXML
    private Label statusLabel;

    @FXML
    private Button connectButton;

    @FXML
    private ListView<String> clientsListView;

    private ProtoServer server;
    private ObservableList<String> clients;//a list type that works well with javaFX

    @FXML
    public void initialize() {
        clients = FXCollections.observableArrayList();
        clientsListView.setItems(clients);
    }

    @FXML
    private void handleConnectButton(ActionEvent event) {
        if (server == null) {
            server = new ProtoServer(8080);
            new Thread(() -> {//making a thread to run in the background and listen for clients
                try {
                    server.listen();
                    Platform.runLater(new Runnable() {//run later is used to sync thread updates basically
                        @Override
                        public void run() {
                            updateStatus("Server listening on port 8080");
                            connectButton.setDisable(true);//we don't need to press it anymore after connection
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatus("Failed to start server");
                        e.printStackTrace();//for error handling
                    });
                }
            }).start();
        }
    }

    private void updateStatus(String status) {//this one is for updating the label's text when needed
    	Platform.runLater(new Runnable() {//run later is used to sync thread updates basically
    	    @Override
    	    public void run() {
    	        statusLabel.setText(status);
    	    }
    	});
    }
}
