/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author raoni
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
           Parent parent = loader.load();
           
           Scene mainScene = new Scene(parent);
           primaryStage.setScene(mainScene);
           primaryStage.setTitle("Sample Javafx Aplication");
           primaryStage.show();
       }
       catch(IOException e){
           e.printStackTrace();
       }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
