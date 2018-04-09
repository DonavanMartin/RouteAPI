package Graph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SizeAppLauncher extends Application {
    
    public static void main(String[] args) {
        Application.launch(SizeAppLauncher.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SizeApp.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);  
            stage.setTitle("Size Example");
            
            stage.setMinHeight(250);
            stage.setMinWidth(500);
            
            stage.setMaxHeight(Integer.MAX_VALUE);
            stage.setMaxWidth(Integer.MAX_VALUE);
            
            stage.show();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
