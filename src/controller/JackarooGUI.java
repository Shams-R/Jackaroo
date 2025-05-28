package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class JackarooGUI extends Application{
	private static JackarooView view;
	private static Game game; //Is this okay?0
	private static Stage primaryStage; //Is this okay?
	private static CardView currentlySelectedCard;
	private static ArrayList<MarbleView> selectedMarbles=new ArrayList<>();
	private static ArrayList<CellView> selectedCellViews = new ArrayList<>();
	
	public static CardView getCurrentlySelectedCard(){
		return currentlySelectedCard;
	}
	
	public static Stage getPrimaryStage() {
		return primaryStage;
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
	        	Media sound = new Media(new File("C:/Users/Zbook G8/Desktop/Faroga.m4a").toURI().toString());
	 	       MediaPlayer mediaPlayer = new MediaPlayer(sound);
	 	       mediaPlayer.play();
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
	            	view.updatePlayerHighlights(game.getCurrentPlayerIndex());
	            	
	            	addShowShortcutsButton(primaryStage);
	            	
	            	view.showNotification(primaryStage);
				       
	            	view.setButtonPane(new Pane());
				       
	            }
	            catch(IOException exception) {
	            }
	        }
	    }  );
	    
    	
	       
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
		  
	public static void selectCard(CardView card) {
		if(game.getCurrentPlayerIndex()!=0)return ;
		 try {
			 game.selectCard(card.getCard());
			 
		     if (currentlySelectedCard != null && currentlySelectedCard != card) {
		         currentlySelectedCard.setEffect(null);
		 		 ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), currentlySelectedCard);
				 scaleDown.setToX(1.0);
				 scaleDown.setToY(1.0);
		         scaleDown.play();
		     }
		     
		     else  if(currentlySelectedCard == card) {
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
		        if(game.getCurrentPlayerIndex()==0) {
//		        	view.removePlayButton();
		        	view.showPlayButton();
		        }
		        
		        
		        if(card.getCard().getName().equals("Seven"))
		        	view.showSplitDistance(game, primaryStage);
		 }
		 catch(InvalidCardException e) {
			 JackarooView.showPopMessage(primaryStage, e);
		 }
	 }
	 
	 public static void selectMarble(MarbleView marble, CellView cell) {
		 if(game.getCurrentPlayerIndex()!=0)return ;
		 try {
			 //if(selectedMarbles==null) selectedMarbles = new ArrayList<>();
			 
			 if(selectedMarbles.contains(marble)) {
				 selectedMarbles.remove(marble); //how to remove it from the model??
				 selectedCellViews.remove(cell);
				 game.getSelectedMarbles().remove(marble.getMarble());
		 		 cell.setEffect(null);
		 		 ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), cell);
				 scaleDown.setToX(1.0);
				 scaleDown.setToY(1.0);
		         scaleDown.play();
		         return;
			 }
			 
			 game.selectMarble(marble.getMarble());
		      
		     selectedMarbles.add(marble);
		     selectedCellViews.add(cell);
		     
		     
			 DropShadow strongShadow = new DropShadow();
		        strongShadow.setRadius(30);
		        strongShadow.setOffsetX(0);
		        strongShadow.setOffsetY(0);
		        strongShadow.setColor(Color.valueOf(marble.getMarble().getColour().toString())); // Strong SaddleBrown

		        // Apply effect and pop-up
		        cell.setEffect(strongShadow);
				ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), cell);
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
		
	public static void playCPU() {
		try {
			game.playPlayerTurn(); //play the current player turn which is the CPU 
		} 
		catch (GameException e) {
			view.showPopMessage(primaryStage, e);
		}
	}
	
	//play human once and the 3 CPUs and then wait for it to be called again by the human player and the play button
	public static void playEngine() {
		
		if (game.canPlayTurn() && !playHuman()) return;

		game.endPlayerTurn();          // Human's turn ends
		view.updateView();
		view.updatePlayerHighlights(game.getCurrentPlayerIndex());  // 🌟 Now show it's CPU1's turn
		deselectAll();

		if (game.checkWin() != null) {
			showWinnerPopup(primaryStage, game.checkWin(), game);
			return;
		}

		PauseTransition pause1 = new PauseTransition(Duration.seconds(4));

		pause1.setOnFinished(e1 -> {
			if (game.canPlayTurn()) {
				playCPU();
			}
			
			game.endPlayerTurn(); // CPU1 ends turn
			view.updateView();
			view.updatePlayerHighlights(game.getCurrentPlayerIndex()); // 🌟 Show it's CPU2's turn

			if (game.checkWin() != null) {
				showWinnerPopup(primaryStage, game.checkWin(), game);
				return;
			}
			
			PauseTransition pause2 = new PauseTransition(Duration.seconds(4));

			pause2.setOnFinished(e2 -> {
				if (game.canPlayTurn()) {
					playCPU();
				}
				
				game.endPlayerTurn(); // CPU2 ends turn
				view.updateView();
				view.updatePlayerHighlights(game.getCurrentPlayerIndex()); //  Show it's CPU3's turn

				if (game.checkWin() != null) {
					showWinnerPopup(primaryStage, game.checkWin(), game);
					return;
				}

				PauseTransition pause3 = new PauseTransition(Duration.seconds(4));

				pause3.setOnFinished(e3 -> {
					if (game.canPlayTurn()) {
						playCPU();
					}
					
					game.endPlayerTurn(); // CPU3 ends turn
					view.updateView();
					view.updatePlayerHighlights(game.getCurrentPlayerIndex()); //  Show it's Human's turn
					
					if (game.checkWin() != null) {
						showWinnerPopup(primaryStage, game.checkWin(), game);
						return;
					}

					if (game.getTurn() == 0) {
						view.setHands();
					}

					if (!game.canPlayTurn()) {
						playEngine(); // restart the cycle						
						view.updateView();
					}
				});
				pause3.play();
			});
			pause2.play();
		});
		pause1.play();
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
        
        for(CellView cell : selectedCellViews) {
        	cell.setEffect(null);
	 		 ScaleTransition scaleDown2 = new ScaleTransition(Duration.millis(100), cell);
			 scaleDown2.setToX(1.0);
			 scaleDown2.setToY(1.0);
	         scaleDown2.play();
        }
        
        selectedMarbles.clear();
        selectedCellViews.clear();
        
        view.removePlayButton();
	}
	
public static void showWinnerPopup(Stage owner, Colour winnerColour, Game game) {
	    
	    Label msg = new Label("🏆 " + getName(winnerColour) + " " + winnerColour + " wins!");
	    msg.setWrapText(true);
	    msg.setMaxWidth(380);
	    msg.setTextAlignment(TextAlignment.CENTER);
	    msg.setAlignment(Pos.CENTER);
	    msg.setStyle("-fx-font-size: 60px; -fx-font-family: 'Georgia';");

	    // Set text colour to the winner's colour using a helper method
	    msg.setTextFill(mapWinnerColour(winnerColour));

	    DropShadow dropShadow = new DropShadow(3, Color.color(0, 0, 0, 0.5));
	    msg.setEffect(dropShadow);

	    // --- Buttons Setup ---
	    Button exitButton = new Button("Exit");
	    exitButton.setStyle(
	        "-fx-background-color: #fdf6e3; " +
	        "-fx-text-fill: #5c3b24; " +
	        "-fx-font-weight: bold; " +
	        "-fx-font-size: 24px;"
	    );
	    exitButton.setOnAction(e -> owner.close());

	    HBox buttonBox = new HBox(20, exitButton);
	    buttonBox.setAlignment(Pos.CENTER);
	    BorderPane.setMargin(buttonBox, new Insets(0, 0, 30, 0));

	    // Fireworks Pane 
	    Pane fireworksPane = new Pane();
	    fireworksPane.setPrefHeight(150);

	    // Background Setup
	    Rectangle background = new Rectangle(420, 360);
	    background.setArcWidth(40);
	    background.setArcHeight(40);
	    background.setFill(Color.web("#8b5e3c"));
	    background.setStroke(Color.web("#5c3b24"));
	    background.setStrokeWidth(3);

	    //  Root Layout as BorderPane
	    BorderPane root = new BorderPane();
	    root.setPadding(new Insets(20));
	    root.setTop(msg);
	    root.setCenter(fireworksPane);
	    root.setBottom(buttonBox);

	    // Layer background behind everything
	    StackPane stack = new StackPane(background, root);

	    //  Popup Stage Setup 
	    Scene scene = new Scene(stack, 420, 360);
	    Stage popup = new Stage();
	    popup.initOwner(owner);
	    popup.initModality(Modality.WINDOW_MODAL);
	    popup.setResizable(false);
	    popup.setTitle("Game Over");
	    popup.setScene(scene);
	    try {
	        popup.getIcons().add(new Image("icon.png"));
	    } catch (Exception e) {
	        System.out.println("Icon not found.");
	    }
	    popup.show();

	    // Launch Fireworks after layout 
	    Platform.runLater(() -> {
	        PauseTransition delay = new PauseTransition(Duration.millis(500));
	        delay.setOnFinished(evt -> playFireworks(fireworksPane));
	        delay.play();
	    });
	}

	// Helper method to map the winner's Colour
	private static Color mapWinnerColour(Colour winnerColour) {
	    
	    switch (winnerColour) {
	        case RED:
	            return Color.RED;
	        case BLUE:
	            return Color.BLUE;
	        case GREEN:
	            return Color.GREEN;
	        case YELLOW:
	            return Color.YELLOW;
	       
	        default:
	            return Color.web("#fdf6e3");
	    }
	}


    private static void playFireworks(Pane fireworksPane) {
        Group fireworksGroup = new Group();
        fireworksPane.getChildren().add(fireworksGroup);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),   e -> createExplosion(fireworksPane, fireworksGroup)),
            new KeyFrame(Duration.seconds(0.5), e -> createExplosion(fireworksPane, fireworksGroup))
        );
        timeline.setCycleCount(5);
        timeline.play();
    }

    private static void createExplosion(Pane pane, Group grp) {
        Random random = new Random();
        double width  = pane.getWidth();
        double height = pane.getHeight();
        double x = random.nextDouble() * width;
        double y = random.nextDouble() * height;
        int numParticles = 20 + random.nextInt(10);

        for (int i = 0; i < numParticles; i++) {
            Circle particle = new Circle(3, Color.hsb(random.nextDouble() * 360, 1, 1));
            particle.setCenterX(x);
            particle.setCenterY(y);
            grp.getChildren().add(particle);

            double angle    = 2 * Math.PI * i / numParticles;
            double distance = 50 + random.nextDouble() * 50;
            double dx       = distance * Math.cos(angle);
            double dy       = distance * Math.sin(angle);

            TranslateTransition translate = new TranslateTransition(Duration.seconds(1.5), particle);
            translate.setByX(dx);
            translate.setByY(dy);

            FadeTransition fade = new FadeTransition(Duration.seconds(1.5), particle);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            ParallelTransition pt = new ParallelTransition(translate, fade);
            pt.setOnFinished(ev -> grp.getChildren().remove(particle));
            pt.play();
        }
    }
	
    public static void addShowShortcutsButton(Stage owner) {
	    Scene scene = owner.getScene();
	    Parent root = scene.getRoot();
	    if (!(root instanceof Pane)) {
	        System.err.println("Cannot add shortcuts button: root is not a Pane.");
	        return;
	    }
	    Pane pane = (Pane) root;

	    // 1) Create and style the button to match the board theme
	    Button showShortcuts = new Button("?");
	    showShortcuts.setStyle(
	        "-fx-background-color: #fdf6e3; " +      // soft ivory
	        "-fx-text-fill: #5c3b24; " +             // rich brown
	        "-fx-font-family: 'Georgia'; " +
	        "-fx-font-size: 24px; " +
	        "-fx-font-weight: bold; " +
	        "-fx-background-radius: 20; " +          // pill shape
	        "-fx-border-color: #5c3b24; " +          // border same brown
	        "-fx-border-width: 2;"
	    );
	    DropShadow borderGlow = new DropShadow();
	    borderGlow.setColor(Color.SADDLEBROWN);
	    borderGlow.setWidth(20);
	    borderGlow.setHeight(20);
	    
	    // Hover animations
	    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), showShortcuts);
	    scaleUp.setToX(1.1);
	    scaleUp.setToY(1.1);

	    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), showShortcuts);
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);
	    
	    PauseTransition delay = new PauseTransition(Duration.millis(50));
	    
	    showShortcuts.setOnMouseEntered((MouseEvent e) -> {
	        if (showShortcuts.getEffect() == null) {
	            delay.setOnFinished(event -> {
	            	showShortcuts.setEffect(borderGlow);
	                scaleUp.playFromStart();
	            });
	            delay.playFromStart();
	        }
	    });

	    showShortcuts.setOnMouseExited((MouseEvent e) -> {
	        if (showShortcuts.getEffect() == borderGlow) {
	            delay.stop();
	            showShortcuts.setEffect(null);
	            scaleDown.playFromStart();
	        }
	    });
	   
	    showShortcuts.setTranslateX(-900);
	    showShortcuts.setTranslateY(-490);

	    // 3) Wire up the custom popup (no Alert)
	    showShortcuts.setOnAction(e -> {
	        Label info = new Label(
	            "0 → Field a marble for player 0\n" +
	            "1 → Field a marble for player 1\n" +
	            "2 → Field a marble for player 2\n" +
	            "3 → Field a marble for player 3\n\n"+
	            "Blue is the highlight of the current player\n"+
	            "Green is the highlight of the next player\n\n"
	        );
	        info.setStyle(
	            "-fx-text-alignment: center; " +
	            "-fx-font-family: 'Georgia'; " +
	            "-fx-font-size: 22px;"
	        );
	        info.setWrapText(true);

	        Button close = new Button("Close");
	        close.setStyle(
	            "-fx-background-color: #fdf6e3; " +
	            "-fx-text-fill: #5c3b24; " +
	            "-fx-font-family: 'Georgia'; " +
	            "-fx-font-size: 20px; " +
	            "-fx-background-radius: 15; " +
	            "-fx-border-color: #5c3b24; " +
	            "-fx-border-width: 2;"
	        );

	        VBox box = new VBox(15, info, close);
	        box.setAlignment(Pos.CENTER);
	        box.setPadding(new Insets(20));

	        Rectangle bg = new Rectangle(450, 350);
	        bg.setArcWidth(30);
	        bg.setArcHeight(30);
	        bg.setFill(Color.web("#8b5e3c"));   // rich brown fill
	        bg.setStroke(Color.web("#5c3b24"));
	        bg.setStrokeWidth(3);

	        StackPane content = new StackPane(bg, box);

	        Stage popup = new Stage(StageStyle.TRANSPARENT);
	        popup.initOwner(owner);
	        popup.initModality(Modality.WINDOW_MODAL);

	        Scene popupScene = new Scene(content, 450, 350);
	        popupScene.setFill(Color.TRANSPARENT);
	        popup.setScene(popupScene);

	        // Center the popup over the owner window
	        popup.setX(owner.getX() + (owner.getWidth() - 450) / 2);
	        popup.setY(owner.getY() + (owner.getHeight() - 350) / 2);

	        close.setOnAction(ev -> popup.close());
	        popup.show();
	    });

	    pane.getChildren().add(showShortcuts);
	}
    
	public static String getName(Colour colour){
		for(PlayerView playerView: view.getPlayersView()){
			if(colour.equals(playerView.getPlayer().getColour())){
				return playerView.getPlayer().getName();
			}
		}
		return "";
	}
	
	
	 public static void main(String[] args) {
		launch(args);
	 }

}
