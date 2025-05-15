package controller;

import java.io.IOException;

import engine.Game;
import exception.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.*;

public class JackarooGUI extends Application{

	
	public void start(Stage primaryStage) throws Exception {
		
		// i implemented an icon for the game 
		Image icon=new Image("icon.png");
		primaryStage.getIcons().add(icon);
		
		
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
	            	view.makeHandsView(game.getPlayers());
	            	view.createHomeZones(game.getPlayers());
	            }
	            catch(IOException exception) {
	            }
	        }
	    }  );
	 
	    // this was to test the popup message
	    Exception e= new CannotFieldException("vrbebverb rvrebeaw rvewrvbrev vwrvwervwa vwvwreav");  
	    view.showPopMessage(primaryStage , e); 
	    
	   
	    
		
	}
	   
	

	
	
	
	

	
	public static void main(String[] args) {
		launch(args);
	}

}
