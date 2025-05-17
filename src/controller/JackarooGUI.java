package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import model.card.Deck;
import model.player.Player;
import engine.Game;
import exception.*;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.*;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class JackarooGUI extends Application{
	private static JackarooView view;
	private static Game game; //Is this okay?
	private static Stage primaryStage; //Is this okay?
	private static CardView currentlySelectedCard;
	private static ArrayList<MarbleView> selectedMarbles=new ArrayList<>();
	
//	private static boolean 
	public static CardView getCurrentlySelectedCard(){
		return currentlySelectedCard;
	}
	public static ArrayList<MarbleView> getSelectedMarbles(){
		return selectedMarbles;
	}
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		// i implemented an icon for the game 
		Image icon=new Image("icon.png");
		primaryStage.getIcons().add(icon);
		
		
		view = new JackarooView(game);
		
		TextField nameField = new TextField();
		AtomicReference<String> selectedGender = new AtomicReference<>(); 
		
		Button start = view.onGameStart(primaryStage, nameField, selectedGender);
		
	    start.setOnMouseClicked(new EventHandler<Event>() {
	        public void handle(Event e) {
	            String playerName = nameField.getText();
	            if (playerName.equals("") || selectedGender.get().equals("")) {
	            	Stage alertStage = new Stage();
	            	// Text message
	            	Text alert = new Text("Please enter your name and gender!");
	            	alert.setFill(Color.web("#fdf6e3")); // Soft ivory
	            	alert.setStyle("-fx-font-size: 20px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;");
	            	alert.setTextAlignment(TextAlignment.CENTER);

	            	// Container for the message
	            	VBox content = new VBox(alert);
	            	content.setAlignment(Pos.CENTER);
	            	content.setSpacing(10);

	            	// Styled background rectangle
	            	Rectangle background = new Rectangle(450, 150);
	            	background.setArcWidth(40);
	            	background.setArcHeight(40);
	            	background.setFill(Color.web("#8b5e3c")); // Rich brown
	            	background.setStroke(Color.web("#5c3b24")); // Deeper brown
	            	background.setStrokeWidth(3);

	            	// StackPane for layering
	            	StackPane alertPane = new StackPane(background, content);
	            	alertPane.setPadding(new Insets(20));

	            	// Scene and stage setup
	            	Scene alertScene = new Scene(alertPane, 450, 150);
	            	alertStage.setScene(alertScene);
	            	alertStage.setTitle("Invalid Input");
	            	alertStage.setResizable(false);
	            	alertStage.initModality(Modality.APPLICATION_MODAL);
	            	
	        	    Image icon = new Image("icon.png");
	        	    alertStage.getIcons().add(icon);
	        	    
	            	alertStage.show();

	            }
	            
	            view.setPlayerName(playerName);
	            String gender = selectedGender.get();
	            view.setPlayerGender(gender);
	            
	            try {
	            	game = new Game(playerName);
	            	view.setGame(game);
	            	view.initializeBoard(primaryStage, game.getPlayers(), game.getBoard().getTrack(), game.getBoard().getSafeZones());
	            	view.makeHandsView(game.getPlayers());
	            	view.showPlayers(game.getPlayers());
	            	int numberOfCards=Deck.getPoolSize();
	            	view.createCardsPool(numberOfCards);
	            	view.putFirePit();
	            
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
    	    	
    		view.field();
    	    }
    	    }
	);
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
	 
	
	 public static void selectCard(CardView card) {
		 try {
			 game.selectCard(card.getCard());
			 
		     if (currentlySelectedCard != null && currentlySelectedCard != card) {
		         currentlySelectedCard.setEffect(null);
		 		 ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), currentlySelectedCard);
				 scaleDown.setToX(1.0);
				 scaleDown.setToY(1.0);
		         scaleDown.play();
		     }
		     
		     else if(currentlySelectedCard == card) {
		         currentlySelectedCard.setEffect(null);
		 		 ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), currentlySelectedCard);
				 scaleDown.setToX(1.0);
				 scaleDown.setToY(1.0);
		         scaleDown.play();
		    	 currentlySelectedCard = null;
		    	 view.removePlayButton();
		         return;
		     }
		      
		     currentlySelectedCard = card;
		        
			 DropShadow strongShadow = new DropShadow();
		        strongShadow.setRadius(30);
		        strongShadow.setOffsetX(0);
		        strongShadow.setOffsetY(0);
		        strongShadow.setColor(Color.rgb(139, 69, 19, 0.9)); // Strong SaddleBrown

		        // Apply effect and pop-up
		        card.setEffect(strongShadow);
				ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), card);
				scaleUp.setToX(1.1);
				scaleUp.setToY(1.1);
		        scaleUp.play();
		        
		        view.showPlayButton();
		        
		        if(card.getCard().getName().equals("Seven"))
		        	view.showSplitDistance(game, primaryStage);
		 }
		 catch(InvalidCardException e) {
			 JackarooView.showPopMessage(primaryStage, e);
		 }
	 }
	 
	 public static void selectMarble(MarbleView marble) {
		 try {
			 //if(selectedMarbles==null) selectedMarbles = new ArrayList<>();
			 
			 if(selectedMarbles.contains(marble)) {
				 selectedMarbles.remove(marble); //how to remove it from the model??
				 game.getSelectedMarbles().remove(marble.getMarble());
		 		 marble.setEffect(null);
		 		 ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), marble);
				 scaleDown.setToX(1.0);
				 scaleDown.setToY(1.0);
		         scaleDown.play();
		         return;
			 }
			 
			 game.selectMarble(marble.getMarble());
		      
		     selectedMarbles.add(marble);
		     
		     
			 DropShadow strongShadow = new DropShadow();
		        strongShadow.setRadius(30);
		        strongShadow.setOffsetX(0);
		        strongShadow.setOffsetY(0);
		        strongShadow.setColor(Color.valueOf(marble.getMarble().getColour().toString())); // Strong SaddleBrown

		        // Apply effect and pop-up
		        marble.setEffect(strongShadow);
				ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), marble);
				scaleUp.setToX(1.1);
				scaleUp.setToY(1.1);
		        scaleUp.play();
		 }
		 catch(InvalidMarbleException e) {
			 JackarooView.showPopMessage(primaryStage, e);
		 }
	 }
	 
	public static void main(String[] args) {
		launch(args);
	}

}
