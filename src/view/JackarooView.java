package view;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import controller.JackarooGUI;
import engine.Game;
import engine.board.Cell;
import engine.board.SafeZone;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import model.card.Card;
import model.card.standard.Ace;
import model.card.wild.Burner;
import model.player.Marble;
import model.player.Player;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.PlayerView.*;
public class JackarooView {
	private String playerName;
	private String playerGender;
	private StackPane mainLayout;
	private TrackView trackView;
	private ArrayList<SafeZoneView> safeZonesView;
	private ArrayList<PlayerHandView> playersHandsView;
	private ArrayList<HomeZoneView> homeZonesView;
	private CardsPoolView cardsPool;
	private FirePitView firePit;
	private Pane buttonPane;
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String name) {
		playerName = name;
	}
	
	public String getPlayerGender() {
		return playerGender;
	}

	public void setPlayerGender(String playerGender) {
		this.playerGender = playerGender;
	}

	public StackPane getMainLayout() {
		return mainLayout;
	}

	public Button onGameStart(Stage stage, TextField nameField, AtomicReference<String> selectedGender) {
	    // Main container
	    mainLayout = new StackPane();

	    // Background image view setup
	    Image background = new Image("Start.png");
	    ImageView view = new ImageView(background);
	    view.setPreserveRatio(false);
	    view.setFitWidth(Screen.getPrimary().getBounds().getWidth());
	    view.setFitHeight(Screen.getPrimary().getBounds().getHeight());

	    // Brown themed rectangle background
	    Rectangle r = new Rectangle();
	    r.setWidth(500);
	    r.setHeight(350);
	    r.setArcWidth(40);
	    r.setArcHeight(40);
	    r.setFill(Color.web("#8b5e3c")); // rich brown
	    r.setStroke(Color.web("#5c3b24")); // deeper brown stroke
	    r.setStrokeWidth(3);

	    // VBox to hold input UI
	    VBox userInput = new VBox(20);
	    userInput.setAlignment(Pos.CENTER);

	    Label title = new Label("Enter Your Name:");
	    title.setTextFill(Color.web("#fdf6e3")); // soft ivory for contrast
	    title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

	    nameField.setMaxWidth(250);
	    nameField.setStyle(
	        "-fx-background-color: rgba(255,255,255,0.7);" +
	        "-fx-font-size: 18px;" +
	        "-fx-font-family: 'Georgia';" +
	        "-fx-text-fill: #000;"
	    );

	    // Gender selection
	    Label genderLabel = new Label("Select Gender:");
	    genderLabel.setTextFill(Color.web("#fdf6e3")); // ivory
	    genderLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Georgia';");

	    ToggleGroup genderGroup = new ToggleGroup();
	    RadioButton male = new RadioButton("Male");
	    male.setToggleGroup(genderGroup);
	    male.setStyle("-fx-font-size: 16px; -fx-font-family: 'Georgia'; -fx-text-fill: #fdf6e3;");

	    RadioButton female = new RadioButton("Female");
	    female.setToggleGroup(genderGroup);
	    female.setStyle("-fx-font-size: 16px; -fx-font-family: 'Georgia'; -fx-text-fill: #fdf6e3;");

	    HBox genderBox = new HBox(20, male, female);
	    genderBox.setAlignment(Pos.CENTER);

	    // Play button
	    Button submitButton = new Button("Play");
	    submitButton.setStyle(
	        "-fx-font-size: 18px;" +
	        "-fx-padding: 10px 25px;" +
	        "-fx-background-color: #d1964d;" +  // sunset yellow
	        "-fx-text-fill: white;" +
	        "-fx-font-family: 'Georgia';" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10;"
	    );

	    // Save selected gender when button is clicked
	    submitButton.setOnAction(e -> {
	        RadioButton selected = (RadioButton) genderGroup.getSelectedToggle();
	        if (selected != null) {
	            selectedGender.set(selected.getText());
	        }
	        else 
	        	selectedGender.set("");
	    });

	    userInput.getChildren().addAll(title, nameField, genderLabel, genderBox, submitButton);

	    StackPane inputContainer = new StackPane(r, userInput);
	    inputContainer.setAlignment(Pos.CENTER);

	    mainLayout.getChildren().addAll(view, inputContainer);
	    Scene s = new Scene(mainLayout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
	    stage.setScene(s);
	    stage.setTitle("Jackaroo");
	    stage.setFullScreen(true);
	    stage.show();

	    return submitButton;
	}

	public void initializeBoard(Stage stage, ArrayList<Player> players, ArrayList<Cell> track, ArrayList<SafeZone> safeZones) {
	    // Background image view setup
	    Image background = new Image("Background.png");
	    ImageView view = new ImageView(background);
	    view.setPreserveRatio(false);
	    view.setFitWidth(Screen.getPrimary().getBounds().getWidth());
	    view.setFitHeight(Screen.getPrimary().getBounds().getHeight());

	    mainLayout.getChildren().clear();
	    mainLayout.getChildren().add(view);

	    // Create a fixed-size GridPane
	    Pane board = new Pane();
	    mainLayout.getChildren().add(board);
	    mainLayout.setAlignment(board, Pos.CENTER);

	    // Set fixed size for the GridPane
	    board.setPrefSize(1050, 1050);
	    board.setMinSize(1000, 1050);
	    board.setMaxSize(1050, 1050);
	    
	    // Set background image for the board
	    Image boardImg = new Image("Board.png");
	    ImageView boardBg = new ImageView(boardImg);
	    boardBg.setPreserveRatio(false);
	    boardBg.setFitWidth(1050);
	    boardBg.setFitHeight(1050);
	    
	    // shift it 100px to the right
	    boardBg.setTranslateX(-25);

	    // Add behind cells:
	    board.getChildren().add(0, boardBg);

	    // Add track cells
	    addTrack(board, players, track);
	    addSafeZones(board, players, safeZones);
	    addHomeZones(players);
	}
    
    private void addSafeZones(Pane board, ArrayList<Player> players, ArrayList<SafeZone> safeZones) {
    	safeZonesView = new ArrayList<SafeZoneView>();
    	
    	for(int i=0; i<4; i++) {
        	int entry = (i*25+98)%100;
        	
        	CellView entryCell = trackView.getMainTrack().get(entry);
        	int entryX = (int) entryCell.getX();
        	int entryY = (int) entryCell.getY();
    		
        	SafeZoneView safeZone = new SafeZoneView(safeZones.get(i).getCells(), players.get(i).getColour().toString(), entryX, entryY, i);
    		safeZonesView.add(safeZone);
    		
            for (CellView cell : safeZone.getSafeZoneView()) {
                board.getChildren().add(cell);
            }
    	}
    }
    
    private void addTrack(Pane board, ArrayList<Player> players, ArrayList<Cell> track) {
    	trackView = new TrackView(track, players);
    	
    	ArrayList<CellView> mainTrack = trackView.getMainTrack();
    	
    	for(CellView cell : mainTrack) {
    		
    		board.getChildren().add(cell);
    	}
    }
    
    
    public void makeHandsView(ArrayList<Player>players){
    	playersHandsView=new ArrayList<>();
    	for(int i=0;i<4;i++){
    	ArrayList<Card>cards=players.get(i).getHand();
    	PlayerHandView hand = new PlayerHandView(cards);
    	playersHandsView.add(hand);
    	}
    	
  
         PlayerHandView bottomPlayer = playersHandsView.get(0);
         PlayerHandView rightPlayer  = playersHandsView.get(1);
         PlayerHandView topPlayer    = playersHandsView.get(2);
         PlayerHandView leftPlayer   = playersHandsView.get(3);
         
         for(int i=1;i<4;i++){
        	 playersHandsView.get(i).showBack();;
         }

         // Orient cards correctly
         leftPlayer.setRotate(90);
         rightPlayer.setRotate(-90);
         topPlayer.setRotate(180);
         BorderPane root = new BorderPane();
         root.setPrefSize(1200,1200); 
  	     root.setMaxSize(1200,1200);
         
         root.setBottom(bottomPlayer);
		 BorderPane.setAlignment(bottomPlayer, Pos.CENTER);
		 bottomPlayer.setTranslateX(-35);
		 bottomPlayer.setTranslateY(5);
		 
		 
		 root.setRight(rightPlayer);
		 BorderPane.setAlignment(rightPlayer, Pos.CENTER);
		 rightPlayer.setTranslateX(100);
		 rightPlayer.setTranslateY(-10);
		 
		 root.setTop(topPlayer);
		 BorderPane.setAlignment(topPlayer, Pos.CENTER);
		 topPlayer.setTranslateX(-30);
		 topPlayer.setTranslateY(-20);
		 
		 root.setLeft(leftPlayer);
		 BorderPane.setAlignment(leftPlayer, Pos.CENTER);
		 leftPlayer.setTranslateX(-160);
		 leftPlayer.setTranslateY(-10);
		 root.setPickOnBounds(false);
		 root.setMouseTransparent(false);
         mainLayout.getChildren().add(root);    	
    }
    public void createCardsPool(int numberOfCards){
    	 cardsPool=new CardsPoolView(numberOfCards);
    	 StackPane.setAlignment(cardsPool, Pos.CENTER_RIGHT);
    	 cardsPool.setTranslateX(-200);
    	 mainLayout.getChildren().add(cardsPool);
    	
    }
    
   public void addHomeZones(ArrayList<Player>players){
	   homeZonesView = new ArrayList<>();
	   BorderPane root = new BorderPane();
	   root.setPrefSize(600,600); 
	   root.setMaxSize(600,600);
	   
    	for(int i=0;i<4;i++){
    		ArrayList<Marble>marbles = players.get(i).getMarbles();
    		ArrayList<CellView>cellsview = new ArrayList<>();  
    		for(int j=0;j<4;j++)
    		{
    			CellView cell=new CellView(players.get(i).getColour().toString());
    			MarbleView marbleview=new MarbleView(marbles.get(j));
                marbleview.setOnMouseClicked(new EventHandler<Event>() {
    				@Override
    				public void handle(Event event) {
    					JackarooGUI.selectMarble(marbleview);
    				}
                });
    			cell.setMarbleView(marbleview);
    			cellsview.add(cell);
    		}
    		
    		HomeZoneView homeZoneView=new HomeZoneView(cellsview);
    		homeZonesView.add(homeZoneView);
    		
    		if(i==0){
    			root.setBottom(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-30);
    			homeZoneView.setTranslateY(-25);
    			
    			
    		}
    		if(i==1){
    			root.setRight(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-45);
    			homeZoneView.setTranslateY(-5);
    			
    			}
    		
    		if(i==2){
    			root.setTop(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-30);
    			homeZoneView.setTranslateY(14);
    		}
    		
    		if(i==3){
    			root.setLeft(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-15);
    		}
    		
    	}
    	root.setPickOnBounds(false);
		root.setMouseTransparent(false);
    	
    	mainLayout.getChildren().add(root);
    }
   public void showPlayers(ArrayList<Player> players){
	   	AnchorPane root=new AnchorPane();    
	   	for(int i=0;i<4;i++){
	          
	           	//topLeft
	               if(i==0 ){
	            	PlayerView playerView=new PlayerView(players.get(i),getPlayerGender());
	               	playerView.setPrefSize(100, 100);
	               	AnchorPane.setTopAnchor(playerView, 0.0);
	               	AnchorPane.setLeftAnchor(playerView, 0.0);
	               	root.getChildren().add(playerView);
	                   }
	               if(i== 1){ 
	            	PlayerView playerView=new PlayerView(players.get(i),"");
	               	playerView.setPrefSize(100, 100);
	               	AnchorPane.setTopAnchor(playerView, 0.0);
	               	AnchorPane.setRightAnchor(playerView, 0.0);
	               	root.getChildren().add(playerView);
	                  
	               }
	               if(i==2){
	               PlayerView playerView=new PlayerView(players.get(i),"");
	               	playerView.setPrefSize(100, 100);
	               	AnchorPane.setBottomAnchor(playerView, 0.0);
	               	AnchorPane.setRightAnchor(playerView, 0.0);
	               	root.getChildren().add(playerView);
	                   }
	               if(i==3){
	            	PlayerView playerView=new PlayerView(players.get(i),"");
	               	playerView.setPrefSize(100, 100);
	               	AnchorPane.setBottomAnchor(playerView, 0.0);
	               	AnchorPane.setLeftAnchor(playerView, 0.0);
	               	root.getChildren().add(playerView);
	                  
	           }
	   }
	   	root.setPickOnBounds(false);
		root.setMouseTransparent(false);
	   	mainLayout.getChildren().add(root);
	   }

  


	public ArrayList<HomeZoneView> getHomeZonesView() {
		return homeZonesView;
	}
	
	public static void showPopMessage(Stage owner, Exception e) {
	    // Label with styled text
	    Label msg = new Label(e.getMessage());
	    msg.setWrapText(true);
	    msg.setMaxWidth(380);
	    msg.setTextAlignment(TextAlignment.CENTER);
	    msg.setAlignment(Pos.CENTER);
	    msg.setTextFill(Color.web("#fdf6e3")); // soft ivory
	    msg.setStyle("-fx-font-size: 18px; -fx-font-family: 'Georgia';");
	
	    // VBox to hold content
	    VBox content = new VBox(msg);
	    content.setAlignment(Pos.CENTER);
	    content.setSpacing(20);
	
	    // Styled background rectangle
	    Rectangle background = new Rectangle(400, 180);
	    background.setArcWidth(40);
	    background.setArcHeight(40);
	    background.setFill(Color.web("#8b5e3c")); // rich brown
	    background.setStroke(Color.web("#5c3b24")); // deeper brown
	    background.setStrokeWidth(3);
	
	    // StackPane for layering rectangle and text
	    StackPane root = new StackPane(background, content);
	    root.setPadding(new Insets(20));
	
	    // Create the popup window
	    Stage popup = new Stage();
	    popup.initOwner(owner);
	    popup.initModality(Modality.WINDOW_MODAL);
	    popup.setResizable(false);
	    popup.setTitle("Wrong Move");
	
	    Scene scene = new Scene(root, 400, 180);
	    popup.setScene(scene);
	
	    // Optional icon
	    Image icon = new Image("icon.png");
	    popup.getIcons().add(icon);
	
	    popup.setOnCloseRequest(evt -> popup.hide());
	    popup.show();
	}
	
	public void putFirePit(){
		firePit=new FirePitView();
		firePit.setTranslateX(-25);
		mainLayout.getChildren().add(firePit);
	}
	
	public TrackView getTrackView() {
		return trackView;
	}	
	
	public void showPlayButton() {
	    Button playButton = new Button("Play");
	    playButton.setStyle(
	        "-fx-font-size: 26px;" +
	        "-fx-padding: 10px 25px;" +
	        "-fx-background-color: #8b5e3c;" +  // sunset yellow
	        "-fx-text-fill: white;" +
	        "-fx-font-family: 'Georgia';" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10;"
	    );
	    
	    playButton.setOnAction(e -> {
	        //Call the method act in the GUI
	    });
	   
	    buttonPane = new Pane();
	    
	    buttonPane.setPickOnBounds(false); // prevents clicks on empty space
	    
	    buttonPane.getChildren().add(playButton);
	    playButton.setTranslateY(500);
	    playButton.setTranslateX(100);
	    playButton.setPrefSize(180, 80);
	    
		// Border effect
		DropShadow borderGlow = new DropShadow();
		borderGlow.setColor(Color.BROWN);
		borderGlow.setWidth(20);
		borderGlow.setHeight(20);

		// Hover animation
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), playButton);
		scaleUp.setToX(1.1);
		scaleUp.setToY(1.1);

		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), playButton);
		scaleDown.setToX(1.0);
		scaleDown.setToY(1.0);

		// Delay before animation triggers
		PauseTransition delay = new PauseTransition(Duration.millis(50));
		
		playButton.setOnMouseEntered((MouseEvent e) -> {
		    if (playButton.getEffect() == null) { // only apply hover effect if no selection effect
		        delay.setOnFinished(event -> {
		            playButton.setEffect(borderGlow);
		            scaleUp.playFromStart();
		        });
		        delay.playFromStart();
		    }
		});

		playButton.setOnMouseExited((MouseEvent e) -> {
		    if (playButton.getEffect() == borderGlow) { // only remove if it's the hover effect
		        delay.stop();
		        playButton.setEffect(null);
		        scaleDown.playFromStart();
		    }
		});
	    mainLayout.getChildren().add(buttonPane);
	}
	
	public void removePlayButton() {
		mainLayout.getChildren().remove(buttonPane);
	}
}