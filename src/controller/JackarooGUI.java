package controller;

import java.io.IOException;

import engine.Game;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.*;

public class JackarooGUI extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		JackarooView view = new JackarooView();
		
		TextField nameField = new TextField();
		
		Button start = view.onGameStart(primaryStage, nameField);
		
	    start.setOnMouseClicked(new EventHandler<Event>() {
	        public void handle(Event e) {
	            String playerName = nameField.getText();
	            if (playerName.equals("")) {
	                Stage alertStage = new Stage();
	                StackPane alertPane = new StackPane();
	                Text alert = new Text("Please enter your name!");
	                alert.setFill(Color.RED);
	                alert.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
	                Image alertbg = new Image("Alert.jpg");
	                ImageView alertView = new ImageView(alertbg);
	                alertPane.getChildren().addAll(alertView, alert);
	                Scene alertScene = new Scene(alertPane, 303, 200);
	                alertStage.setScene(alertScene);
	                alertStage.setTitle("Error");
	                alertStage.setResizable(false);
	                alertStage.show();
	                return;
	            }
	            view.setPlayerName(playerName);
	            
	            try {
	            	Game game = new Game(playerName);
	            	view.initializeBoard(primaryStage, game.getPlayers());
	            }
	            catch(IOException exception) {
	            }
	        }
	    });
		
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
