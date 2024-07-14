package ServerGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import server.ProtoServer;
import javafx.scene.layout.GridPane;

public class serverController {
	private ProtoServer server;
	@FXML
    private GridPane gridPane;
    @FXML
    private Label statusLabel;
    @FXML
    private Button connectButton;
    @FXML
    private Button disconnectButton;
    @FXML
    private ListView<String> clientsListView;
    
    //Initialize the serverGUI default values
    @FXML
    public void initialize() {
    	//Disable the disconnect button initially
        disconnectButton.setDisable(true);
        gridPane.setPadding(new Insets(10));
    }

    //Starts up the server
    @FXML
	private void handleConnectButton(ActionEvent event) {
		if (server == null) {
            server = new ProtoServer(8080);
            server.setController(this);
          //making a thread to run in the background and listen for clients
            new Thread(() -> {
                try {
                    server.listen();
                  //run later is used to sync thread updates basically
                    Platform.runLater(new Runnable() {
                    	//Turns off and on the connect and disconnect buttons respectively
                        @Override
                        public void run() {
                            connectButton.setDisable(true);
                            disconnectButton.setDisable(false); 
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        updateStatus("Failed to start server");
                        e.printStackTrace();
                    });
                }
            }).start();
        }
    }

    //Turns off the server
    @FXML
    private void handleDisconnectButton(ActionEvent event) {
        if (server != null) {
            try {
                server.stopListening(); // Stop the server from listening for new clients
                server.close(); // Close the server
                server = null;
                updateStatus("Server disconnected");
                connectButton.setDisable(false); // Enable the connect button
                disconnectButton.setDisable(true); // Disable the disconnect button
            } catch (Exception e) {
                e.printStackTrace();
                updateStatus("Failed to stop server: " + e.getMessage());
            }
        }
    }

   
  //this one is for updating the label's text when needed
    public void updateStatus(String status) {
    	Platform.runLater(new Runnable() {//run later is used to sync thread updates basically
    	    @Override
    	    public void run() {
    	        statusLabel.setText(status);
    	    }
    	});
    }
    
    //Display the clients details upon connection/disconnection on the ListView screen
    public void displayClientDetails(String[] msg) {
        Platform.runLater(() -> {
            StringBuilder str = new StringBuilder();
            for (String s : msg) {
                str.append(s).append(" ");
            }
            clientsListView.getItems().add(str.toString());
        });
    }
    
  //Receive instance of server and send for him this instance of controller 
    public void setProtoServer(ProtoServer protoServer) {
    	protoServer.setController(this);
    	
     }
    
}
