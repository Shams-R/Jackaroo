package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
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
import javafx.scene.layout.HBox;
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
	private static Game game; //Is this okay?0
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
	public static void handleTrap(Marble marble){
		 MarbleView marbleView =view.getMarbleView(marble);
		 view.destroy(marbleView);
		 
		 }
	 public static void updateCardsPool(int n){
		 view.getCardsPool().setCards(n);
		 
		 }
		  public static void clearFirePit(){
		 view.getFirePit().clear();
		 
		 }


	   
	
		  public static void fieldShortcut(TrackView mainTrack,ArrayList<HomeZoneView> homeZones,Stage owner,JackarooView view,Game game) {
				Scene scene = owner.getScene();

					scene.setOnKeyPressed(event -> {
					switch (event.getCode()) {
				case DIGIT0:
						try {	
							game.fieldMarble(0);
							view.updateView();
						} catch (CannotFieldException | IllegalDestroyException e) {
							
							view.showPopMessage(primaryStage, e) ;
						}
					break;
				case DIGIT1:
					
						try {
							game.fieldMarble(1);
							view.updateView();
						}
						catch (CannotFieldException | IllegalDestroyException e) {
							
							view.showPopMessage(primaryStage, e) ;
						}
					break;
				case DIGIT2:
					
						try {
							game.fieldMarble(2);
							view.updateView();
						} catch (CannotFieldException | IllegalDestroyException e) {
							
							view.showPopMessage(primaryStage, e) ;
						}
					break;
				case DIGIT3:
					
					try {
						game.fieldMarble(3);
						view.updateView();
					} catch (CannotFieldException | IllegalDestroyException e) {
						
						view.showPopMessage(primaryStage, e) ;
					}
					break;
					}
					});
		}

    	    
	
    	


//	 public static void fieldingMechanism(TrackView trackView ,ArrayList<HomeZoneView> homeZones,Stage owner,JackarooView view  ,Game game){
//		   
//		   Platform.runLater(() -> {
//			   
//		   try{ 
//			
//			   
//			   game.fieldMarble();
//			   int i= game.getCurrentPlayerIndex();
//			   HomeZoneView homeZone = homeZones.get(i);
//			   MarbleView marble=homeZone.fieldMarble() ;
//			   trackView.getMainTrack().get(i*25).setMarbleView(marble) ;
//		   
//		
//			} catch (CannotFieldException | IllegalDestroyException e) {
//				view.showPopMessage(owner, e);
//			}
//		  
//	   }
//		   );
//	   }
	 
	
	 public static void main(String[] args) {
		launch(args);
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
		        
		        //Only if its the turn of the current play
		        if(game.getCurrentPlayerIndex()==0)
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
	 
	public static boolean playHuman() {
		try {
			game.playPlayerTurn();
			
		//	view.act(currentlySelectedCard, selectedMarbles);
			
			
			return true;
		}
		catch(GameException e) {
			    // Exception message label
			    Label exceptionLabel = new Label(e.getMessage());
			    exceptionLabel.setWrapText(true);
			    exceptionLabel.setMaxWidth(380);
			    exceptionLabel.setTextAlignment(TextAlignment.CENTER);
			    exceptionLabel.setAlignment(Pos.CENTER);
			    exceptionLabel.setTextFill(Color.web("#fdf6e3")); // soft ivory
			    exceptionLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Georgia';");
			    
			    // Discard prompt label
			    Label discardLabel = new Label("Do you want to discard this card?");
			    discardLabel.setWrapText(true);
			    discardLabel.setMaxWidth(380);
			    discardLabel.setTextAlignment(TextAlignment.CENTER);
			    discardLabel.setAlignment(Pos.CENTER);
			    discardLabel.setTextFill(Color.web("#fdf6e3")); // soft ivory
			    discardLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Georgia';");

			    // Create the buttons
			    Button yesButton = new Button("Yes");
			    Button noButton = new Button("No");

			    yesButton.setStyle("-fx-background-color: #fdf6e3; -fx-text-fill: #5c3b24; -fx-font-weight: bold;");
			    noButton.setStyle("-fx-background-color: #fdf6e3; -fx-text-fill: #5c3b24; -fx-font-weight: bold;");

			    // Layout for the buttons (side by side)
			    HBox buttonBox = new HBox(10, yesButton, noButton);
			    buttonBox.setAlignment(Pos.CENTER);

			    // Combine both messages and the buttons in a VBox
			    VBox content = new VBox(20, exceptionLabel, discardLabel, buttonBox);
			    content.setAlignment(Pos.CENTER);

			    // Create the background rectangle
			    Rectangle background = new Rectangle(400, 180);
			    background.setArcWidth(40);
			    background.setArcHeight(40);
			    background.setFill(Color.web("#8b5e3c")); // rich brown
			    background.setStroke(Color.web("#5c3b24")); // deep brown
			    background.setStrokeWidth(3);

			    // Use a StackPane to layer the background and content
			    StackPane root = new StackPane(background, content);
			    root.setPadding(new Insets(20));

			    // Create the popup stage
			    Stage popup = new Stage();
			    popup.initOwner(primaryStage);
			    popup.initModality(Modality.WINDOW_MODAL);
			    popup.setResizable(false);
			    popup.setTitle("Discard Card");

			    Scene scene = new Scene(root, 400, 180);
			    popup.setScene(scene);
			    	
			    // icon 
			    Image icon = new Image("icon.png");
			    popup.getIcons().add(icon);

			   
			    AtomicBoolean returnValue = new AtomicBoolean(false);

			    // Button actions
			    yesButton.setOnAction(event -> {
			        returnValue.set(true);
			        popup.close();
			    });
			    noButton.setOnAction(event -> {
			        returnValue.set(false);
			        popup.close();
			    });

			    // Shows the popup and waits until the user closes it
			    popup.showAndWait();

			    // Return true if "Yes" was clicked; false otherwise
			    return returnValue.get();
		}
	}
		
	public static void playCPU(PlayerView CPU) {
		CPU cpu = (CPU) CPU.getPlayer();
		
		try {
			game.playPlayerTurn(); //play the current player turn which is the CPU 
		} 
		catch (GameException e) {
			view.showPopMessage(primaryStage, e);
		}
		
	/*	if(cpu.isPlayed()) {
			
			ArrayList<Marble> selectedMarbles = cpu.getSelectedMarbles();
			Card selectedCard = cpu.getSelectedCard();
			
			if(selectedMarbles!=null && selectedCard!=null) {
				
				ArrayList<CellView> mainTrack = view.getTrackView().getMainTrack();
				
				ArrayList<MarbleView> selectedMarblesView = new ArrayList<>();
				
				for(CellView cellView : mainTrack) {
					System.out.println(cellView);
					if(cellView.getMarbleView()!=null && cellView.getMarbleView().getMarble()!=null && selectedMarbles!=null && selectedMarbles.contains(cellView.getMarbleView().getMarble()))
						selectedMarblesView.add(cellView.getMarbleView());
				}
				
				ArrayList<CellView> homeZone = view.getHomeZoneView(CPU).getCells();
				ArrayList<CellView> safeZone = view.getHomeZoneView(CPU).getCells();
				
				for(CellView cellView : homeZone) {
					System.out.println(cellView);
					if(cellView.getMarbleView()!=null && cellView.getMarbleView().getMarble()!=null && selectedMarbles!=null && selectedMarbles.contains(cellView.getMarbleView().getMarble()))
						selectedMarblesView.add(cellView.getMarbleView());
				}
				
				for(CellView cellView : safeZone) {
					if(cellView.getMarbleView()!=null && cellView.getMarbleView().getMarble()!=null && selectedMarbles!=null && selectedMarbles.contains(cellView.getMarbleView().getMarble()))
						selectedMarblesView.add(cellView.getMarbleView());
				}
				
				//to arrange the selectedMarblesViews
				for(int i=0; i<selectedMarblesView.size(); i++) {
					for(int j=0; j<selectedMarblesView.size(); j++) {
						if(selectedMarbles.get(i)==selectedMarblesView.get(i).getMarble()) {
							break;
						}
						
						else {
							selectedMarblesView.add(selectedMarblesView.remove(i));
						}
					}
				}
				
				ArrayList<CardView> hand = view.getPlayerHandView(CPU).getHandCardsView();
				CardView selectedCardView = null;
				
				for(CardView card : hand) {
					if(card.getCard()==selectedCard) {
						selectedCardView = card;
						break;
					}
				}
				
				//finally, act on those marbles using this card
				view.act(selectedCardView, selectedMarblesView);
			}
		}*/
			
	}
	
	//play human once and the 3 CPUs and then wait for it to be called again by the human player and the play button
	public static void playEngine() {
		
		//can play turn??
		if(game.canPlayTurn())
			if(!playHuman()) return;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																			
		//delay
		
		//end player turn 
		game.endPlayerTurn();
		//update hand 
		view.updateView();
		//view.updateHand(0);
		
		//deselectAll
		deselectAll();
		
		//check win
		if(game.checkWin()!=null) 
			showWinnerPopup(primaryStage, game.checkWin(), game);
		
		//cpu1 can play turn
		if(game.canPlayTurn()) {
			//play cpu1
			//playCPU(view.getPlayersView().get(1));
			//delay
			//end player turn
			try {
				game.playPlayerTurn(); //play the current player turn which is the CPU 
			} 
			catch (GameException e) {
				view.showPopMessage(primaryStage, e);
			}
			game.endPlayerTurn();
			//update hand
			//view.updateHand(1);
			view.updateView();
			//check win
			if(game.checkWin()!=null) 
				showWinnerPopup(primaryStage, game.checkWin(), game);
		}
		
		//cpu2 can play turn
		if(game.canPlayTurn()) {
			//play cpu2
			//playCPU(view.getPlayersView().get(2));
			//delay
			//end player turn
			try {
				game.playPlayerTurn(); //play the current player turn which is the CPU 
			} 
			catch (GameException e) {
				view.showPopMessage(primaryStage, e);
			}
			game.endPlayerTurn();
			//update hand
			//view.updateHand(2);
			view.updateView();
			//check win
			if(game.checkWin()!=null) 
				showWinnerPopup(primaryStage, game.checkWin(), game);
		}
		
		//cpu3 can play turn
		if(game.canPlayTurn()) {
			//play cpu3
			//playCPU(view.getPlayersView().get(3));
			//delay
			//end player turn
			try {
				game.playPlayerTurn(); //play the current player turn which is the CPU 
			} 
			catch (GameException e) {
				view.showPopMessage(primaryStage, e);
			}
			game.endPlayerTurn();
			//update hand
			//view.updateHand(3);
			view.updateView();
			//check win
			if(game.checkWin()!=null) 
				showWinnerPopup(primaryStage, game.checkWin(), game);
		}
		
		
		//turn==0? --> set hand
		if(game.getTurn()==0) {
//			//should be replaced by set hand 
//			ArrayList<PlayerHandView> hands = view.getPlayersHandsView();
//			
//			for(int i=0; i<hands.size(); i++) {
//				PlayerHandView hand = hands.get(0);
//				Player player = view.getPlayersView().get(i).getPlayer();
//				hand.setHandCardsView(player.getHand());
//			}
			System.out.println("turn");
			view.setHands();
			//view.setHands();
		}
		
		//human player can play turn?
		//if no, call play engine
		if(!game.canPlayTurn())
			playEngine();
		
		//if yes exit
	}
	
	public static void deselectAll() {
		if(currentlySelectedCard!=null) {
			currentlySelectedCard.setEffect(null);
			ScaleTransition scaleDown1 = new ScaleTransition(Duration.millis(100), currentlySelectedCard);
			scaleDown1.setToX(1.0);
			scaleDown1.setToY(1.0);
			scaleDown1.play();
		}
        currentlySelectedCard = null;
        
        for(MarbleView marble : selectedMarbles) {
	 		 ScaleTransition scaleDown2 = new ScaleTransition(Duration.millis(100), marble);
			 scaleDown2.setToX(1.0);
			 scaleDown2.setToY(1.0);
	         scaleDown2.play();
        }
        
        selectedMarbles.clear();
	}
	
	public static void showWinnerPopup(Stage owner, Colour winnerColour, Game game) {
	    // Label announcing the winner
	    Label msg = new Label("🏆 " + getName(winnerColour) + " (" + winnerColour + ") wins the game!");
	    msg.setWrapText(true);
	    msg.setMaxWidth(380);
	    msg.setTextAlignment(TextAlignment.CENTER);
	    msg.setAlignment(Pos.CENTER);
	    msg.setTextFill(Color.web("#fdf6e3")); // soft ivory
	    msg.setStyle("-fx-font-size: 18px; -fx-font-family: 'Georgia';");

	    // "Play Again" button
	    Button playAgainButton = new Button("Exist");
	    playAgainButton.setStyle(
	        "-fx-background-color: #fdf6e3; " +
	        "-fx-text-fill: #5c3b24; " +
	        "-fx-font-weight: bold; " +
	        "-fx-font-size: 16px;"
	    );

	    // Leave onAction empty for now
	    playAgainButton.setOnAction(e -> {
	        // TODO: Define behavior to restart game
	    	primaryStage.close();
	    });

	    // VBox to hold content
	    VBox content = new VBox(msg, playAgainButton);
	    content.setAlignment(Pos.CENTER);
	    content.setSpacing(20);

	    // Styled background rectangle
	    Rectangle background = new Rectangle(400, 200);
	    background.setArcWidth(40);
	    background.setArcHeight(40);
	    background.setFill(Color.web("#8b5e3c")); // rich brown
	    background.setStroke(Color.web("#5c3b24")); // deeper brown
	    background.setStrokeWidth(3);

	    // StackPane for layering rectangle and content
	    StackPane root = new StackPane(background, content);
	    root.setPadding(new Insets(20));

	    // Create the popup window
	    Stage popup = new Stage();
	    popup.initOwner(owner);
	    popup.initModality(Modality.WINDOW_MODAL);
	    popup.setResizable(false);
	    popup.setTitle("Game Over");

	    Scene scene = new Scene(root, 400, 200);
	    popup.setScene(scene);

	    // Optional icon
	    try {
	        Image icon = new Image("icon.png");
	        popup.getIcons().add(icon);
	    } catch (Exception e) {
	        System.out.println("Icon not found.");
	    }

	    popup.setOnCloseRequest(evt -> popup.hide());
	    popup.show();
	}  
	
	public static String getName(Colour colour){
		for(PlayerView playerView: view.getPlayersView()){
			if(colour.equals(playerView.getPlayer().getColour())){
				return playerView.getPlayer().getName();
			}
		}
		return "";
	}

}
