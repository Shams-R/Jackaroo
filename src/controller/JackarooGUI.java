package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import model.card.Deck;
import engine.Game;
import exception.*;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;

public class JackarooGUI extends Application{

	
	public void start(Stage primaryStage) throws Exception {
		
		// i implemented an icon for the game 
		Image icon=new Image("icon.png");
		primaryStage.getIcons().add(icon);
		
		
		JackarooView view = new JackarooView();
		
		TextField nameField = new TextField();
		AtomicReference<String> selectedGender = new AtomicReference<>(); 
		
		Button start = view.onGameStart(primaryStage, nameField, selectedGender);
		
	    start.setOnMouseClicked(new EventHandler<Event>() {
	        public void handle(Event e) {
	            String playerName = nameField.getText();
	            if (playerName.equals("") || selectedGender.get().equals("")) {
	                Stage alertStage = new Stage();
	                StackPane alertPane = new StackPane();
	                Text alert = new Text("Please enter your name and gender!");
	                alert.setFill(Color.RED);
	                alert.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
	                alertPane.getChildren().addAll(alert);
	                Scene alertScene = new Scene(alertPane, 450, 150);
	                alertStage.setScene(alertScene);
	                alertStage.setTitle("Error");
	                alertStage.setResizable(false);
	                alertStage.show();
	                return;
	            }
	            
	            view.setPlayerName(playerName);
	            String gender = selectedGender.get();
	            view.setPlayerGender(gender);
	            
	            try {
	            	Game game = new Game(playerName);
	            	view.initializeBoard(primaryStage, game.getPlayers(), game.getBoard().getTrack(), game.getBoard().getSafeZones());
	            	view.makeHandsView(game.getPlayers());
	            	view.showPlayers(game.getPlayers());
	            	int numberOfCards=Deck.getPoolSize();
	            	view.createCardsPool(numberOfCards);
	            
	            	fieldShortcut(view.getTrackView(),view.getHomeZonesView(),primaryStage, view  ,game);
	            	
	            	
	            }
	            catch(IOException exception) {
	            }
	        }
	    }  );
	 
	    // this was to test the popup message
	    //Exception e= new CannotFieldException("vrbebverb rvrebeaw rvewrvbrev vwrvwervwa vwvwreav");  
	   // view.showPopMessage(primaryStage , e); 
	}
	   
	
	
	
	
	public static void fieldShortcut(TrackView mainTrack,ArrayList<HomeZoneView> homeZones,Stage owner,JackarooView view  ,Game game) {
		Scene scene = owner.getScene();

    	scene.setOnKeyPressed(event -> {
    	    if (event.getCode() == KeyCode.ENTER) {
    	    	fieldingMechanism(mainTrack,homeZones,owner, view , game);
    	    }
    	    }
    	) ;
    	
    	owner.setScene(scene);
    
 }
	 public static void fieldingMechanism(TrackView trackView ,ArrayList<HomeZoneView> homeZones,Stage owner,JackarooView view  ,Game game){
		   
		   Platform.runLater(() -> {
			   
		   try{ 
			
			   
			   game.fieldMarble();
			   int i= game.getCurrentPlayerIndex();
			   HomeZoneView homeZone = homeZones.get(i);
			   MarbleView marble=homeZone.fieldMarble() ;
			   trackView.getMainTrack().get(i*25).setMarbleView(marble) ;
		   
		
			} catch (CannotFieldException | IllegalDestroyException e) {
				view.showPopMessage(owner, e);
			}
		  
	   }
		   );
	   }


	
	
	
	

	
	public static void main(String[] args) {
		launch(args);
	}

}
