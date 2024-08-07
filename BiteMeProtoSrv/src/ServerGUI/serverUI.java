package ServerGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class serverUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));
        Scene scene = new Scene(root);  
        scene.getStylesheets().add(getClass().getResource("server.css").toExternalForm());
        primaryStage.setTitle("Server GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    public static void main(String[] args) {
        launch(args);
    }
}

