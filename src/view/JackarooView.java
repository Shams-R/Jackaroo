package view;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.print.DocFlavor.URL;

import controller.JackarooGUI;
import engine.Game;
import engine.board.Board;
import engine.board.Cell;
import engine.board.CellType;
import engine.board.SafeZone;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.SplitOutOfRangeException;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.card.standard.Ace;
import model.card.standard.Five;
import model.card.standard.Four;
import model.card.standard.Jack;
import model.card.standard.King;
import model.card.standard.Queen;
import model.card.standard.Seven;
import model.card.standard.Standard;
import model.card.standard.Ten;
import model.card.wild.Burner;
import model.player.Marble;
import model.player.Player;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.PlayerView.*;
public class JackarooView {
	private static String playerName;
	private String playerGender;
	private StackPane mainLayout;
	private TrackView trackView;
	private ArrayList<SafeZoneView> safeZonesView;
	private ArrayList<PlayerHandView> playersHandsView;
	private ArrayList<HomeZoneView> homeZonesView;
	private CardsPoolView cardsPool;
	private FirePitView firePit;
	private Pane buttonPane;
	private BorderPane splitDistancePane;
	private static Game game;
	private static ArrayList<PlayerView> playersView;
	private boolean played;
	
	public CardsPoolView getCardsPool(){
		return cardsPool;
	}
	public FirePitView getFirePit(){
		return firePit;
	}
	
	public void setButtonPane(Pane buttonPane) {
		this.buttonPane = buttonPane;
	}
	
	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public ArrayList<PlayerHandView> getPlayersHandsView() {
		return playersHandsView;
	}

	public ArrayList<PlayerView> getPlayersView() {
		return playersView;
	}

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
	public JackarooView(Game game){
		this.game=game;
	}
	public void setGame(Game game){
		this.game=game;
	}

	public ArrayList<HomeZoneView> getHomeZonesView() {
		return homeZonesView;
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
	    board.setPrefSize(1150, 1150);
	    board.setMinSize(1150, 1150);
	    board.setMaxSize(1150, 1150);
	    
	    // Set background image for the board
	    Image boardImg = new Image("Board.png");
	    ImageView boardBg = new ImageView(boardImg);
	    boardBg.setPreserveRatio(false);
	    boardBg.setFitWidth(1150);
	    boardBg.setFitHeight(1150);
	    
	    // shift it 100px to the right
	    boardBg.setTranslateX(-25);

	    // Add behind cells:
	    board.getChildren().add(0, boardBg);

	    // Add track cells
	    addTrack(board, players, track);
	    addSafeZones(board, players, safeZones);
	    addHomeZones(players);
	}
	
	public void updateView(){
		Board board=game.getBoard();
		
		//first update the track
		ArrayList<Cell>track=board.getTrack();
		ArrayList<CellView> trackViewCells=trackView.getMainTrack();
		update(trackViewCells,track);
		
		//second update safezones
		ArrayList<SafeZone>safeZones=board.getSafeZones();
		
		for(int i=0;i<4;i++){
			SafeZone safeZone=safeZones.get(i);
			SafeZoneView safeZoneView=safeZonesView.get(i);
			ArrayList<CellView> safeZoneViewCells=safeZoneView.getSafeZoneView();
			ArrayList<Cell> safeZoneCells=safeZone.getCells();
			update(safeZoneViewCells,safeZoneCells);
		}
		
		//third update homezones
		for(int i=0;i<4;i++){
			HomeZoneView homeZoneView= homeZonesView.get(i);
			Player player=game.getPlayers().get(i);
			ArrayList<Marble> marbles=player.getMarbles();
			homeZoneView.set(marbles);
		}
		
		//fourth handView
		for(int i=0;i<4;i++){
			
			updateHand(i);
			
	}
		
		//cardspool
		cardsPool.setCards(Deck.getPoolSize());
		
		//firepit
		ArrayList<Card> firePitCards=game.getFirePit();
		firePit.update(firePitCards);
	}
	
	public void update(ArrayList<CellView> cellsView,ArrayList<Cell>cells){
		for(int i=0;i<cells.size();i++){
			MarbleView marbleView=cellsView.get(i).getWithOutRemove();
			Marble marble=cells.get(i).getMarble();
			if(marble==null&&marbleView==null)
				continue;
			if(marble==null)
				cellsView.get(i).getMarbleView(); //this removes the marble
			else if(marbleView==null ||marbleView.getMarble()!=marble ){
				MarbleView marbleView2=new MarbleView(marble);
			       JackarooGUI.sound("Click.m4a");
			       
			       if(cells.get(0).getCellType()==CellType.SAFE) {
				       
				       JackarooGUI.sound("Wa7da.m4a");
			       }
			       
				cellsView.get(i).setMarbleView(marbleView2);
				
			}
			
			}
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
         PlayerHandView leftPlayer   = playersHandsView.get(1);
         PlayerHandView topPlayer    = playersHandsView.get(2);
         PlayerHandView rightPlayer  = playersHandsView.get(3);
         
         
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
             /*   marbleview.setOnMouseClicked(new EventHandler<Event>() {
    				@Override
    				public void handle(Event event) {
    					JackarooGUI.selectMarble(marbleview);
    				}
                });*/
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
    		if(i==3){
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
    		
    		if(i==1){
    			root.setLeft(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-15);
    		}
    		
    	}
    	root.setPickOnBounds(false);
		root.setMouseTransparent(false);
    	
    	mainLayout.getChildren().add(root);
    }
   public void showPlayers(ArrayList<Player> players) {
	    AnchorPane root = new AnchorPane();
	    playersView = new ArrayList<>();
	    
	    for (int i = 0; i < 4; i++) {
	        Player player = players.get(i);

	        // Create PlayerView
	        PlayerView playerView = new PlayerView(player, i == 0 ? getPlayerGender() : "");
	        playersView.add(playerView);
	        
	        playerView.setPrefSize(100, 100);

	        // Create label for the name
	        Label nameLabel = new Label(player.getName());
	        nameLabel.setStyle("-fx-text-fill: " + player.getColour().toString().toLowerCase() + "; " +
	                           "-fx-font-weight: bold; " +
	                           "-fx-font-size: 24px; " +
	                           "-fx-font-family: 'Georgia', 'Garamond', serif;");
	        

	        // Create a wooden frame around the name label
	        StackPane nameFrame = new StackPane(nameLabel);
	        nameFrame.setPadding(new Insets(5)); // space inside the frame
	        nameFrame.setStyle(
	            "-fx-border-color: saddlebrown; " +
	            "-fx-border-width: 4; " +
	            "-fx-border-radius: 6; " +
	            "-fx-background-color: #f5deb3; " + // light wood tone
	            "-fx-background-radius: 6;"
	        );

	        // Use AnchorPane for precise positioning
	        AnchorPane wrapper = new AnchorPane();
	        wrapper.setPrefSize(150, 150);
	        wrapper.getChildren().addAll(playerView, nameFrame);

	        // Position PlayerView inside wrapper
	        AnchorPane.setTopAnchor(playerView, 30.0);
	        AnchorPane.setLeftAnchor(playerView, 25.0);

	        // Position the frame and the wrapper based on corner
	        switch (i) {
	            case 2: // Top-left → label to left
	                AnchorPane.setTopAnchor(nameFrame, 10.0);
	                AnchorPane.setLeftAnchor(nameFrame, -50.0);
	                AnchorPane.setTopAnchor(wrapper, 30.0);
	                AnchorPane.setLeftAnchor(wrapper, 300.0);
	                break;
	            case 3: // Top-right → label to right
	                AnchorPane.setTopAnchor(nameFrame, 0.0);
	                AnchorPane.setLeftAnchor(nameFrame, 200.0);
	                AnchorPane.setTopAnchor(wrapper, 30.0);
	                AnchorPane.setRightAnchor(wrapper, 300.0);
	                break;
	            case 0: // Bottom-right → label to right
	                AnchorPane.setTopAnchor(nameFrame, 0.0);
	                AnchorPane.setLeftAnchor(nameFrame, 200.0);
	                AnchorPane.setBottomAnchor(wrapper, 30.0);
	                AnchorPane.setRightAnchor(wrapper, 300.0);
	                break;
	            case 1: // Bottom-left → label to left
	                AnchorPane.setTopAnchor(nameFrame, 10.0);
	                AnchorPane.setLeftAnchor(nameFrame, -50.0);
	                AnchorPane.setBottomAnchor(wrapper, 30.0);
	                AnchorPane.setLeftAnchor(wrapper, 300.0);
	                break;
	        }

	        root.getChildren().add(wrapper);
	    }

	    root.setPickOnBounds(false);
	    root.setMouseTransparent(false);
	    mainLayout.getChildren().add(root);
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
	
	public void showNotification(Stage primaryStage) {
	    // Label with styled text
	    Label msg = new Label("For shortcut keys, click (?) on the top left");
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
	    popup.initOwner(primaryStage);
	    popup.initModality(Modality.WINDOW_MODAL);
	    popup.setResizable(false);
	    popup.setTitle("Notification");
	
	    Scene scene = new Scene(root, 400, 180);
	    popup.setScene(scene);
	
	    // Optional icon
	    Image icon = new Image("icon.png");
	    popup.getIcons().add(icon);
	
	    popup.setOnCloseRequest(evt -> popup.hide());
	    popup.show();
	}
	
	public static void showTrapCell(Colour colour) {
	    // Label with styled text
		String name = "";
		for(int i=0; i<playersView.size(); i++) if(playersView.get(i).getPlayer().getColour()==colour) name = playersView.get(i).getPlayer().getName();
		if(name.equals(playerName))
			name = "You";
		
	    Label msg = new Label(name + " fell for a trap cell!");
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
	    popup.initOwner(JackarooGUI.getPrimaryStage());
	    popup.initModality(Modality.WINDOW_MODAL);
	    popup.setResizable(false);
	    popup.setTitle("Sorry");
	
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
	        "-fx-background-color: #f5deb3;" +         // Light wood tone
	        "-fx-text-fill: #5C3317;" +                // Dark wooden text
	        "-fx-font-family: 'Georgia';" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10;" +
	        "-fx-border-color: saddlebrown;" +         // Wooden border
	        "-fx-border-width: 4;" +
	        "-fx-border-radius: 10;"
	    );

//	    buttonPane = new Pane();
	    buttonPane.setPickOnBounds(false);
	    buttonPane.getChildren().add(playButton);

	    playButton.setTranslateY(500);
	    playButton.setTranslateX(100);
	    playButton.setPrefSize(180, 80);
	    
	    playButton.setOnAction(e -> {
	    	JackarooGUI.playEngine();
	    });

	    // Border glow effect
	    DropShadow borderGlow = new DropShadow();
	    borderGlow.setColor(Color.SADDLEBROWN);
	    borderGlow.setWidth(20);
	    borderGlow.setHeight(20);

	    // Hover animations
	    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), playButton);
	    scaleUp.setToX(1.1);
	    scaleUp.setToY(1.1);

	    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), playButton);
	    scaleDown.setToX(1.0);
	    scaleDown.setToY(1.0);

	    PauseTransition delay = new PauseTransition(Duration.millis(50));

	    playButton.setOnMouseEntered((MouseEvent e) -> {
	        if (playButton.getEffect() == null) {
	            delay.setOnFinished(event -> {
	                playButton.setEffect(borderGlow);
	                scaleUp.playFromStart();
	            });
	            delay.playFromStart();
	        }
	    });

	    playButton.setOnMouseExited((MouseEvent e) -> {
	        if (playButton.getEffect() == borderGlow) {
	            delay.stop();
	            playButton.setEffect(null);
	            scaleDown.playFromStart();
	        }
	    });
	    
	    if(!mainLayout.getChildren().contains(buttonPane))
	    	mainLayout.getChildren().add(buttonPane);
	}

	
	public void removePlayButton() {
		buttonPane.getChildren().clear();
		mainLayout.getChildren().remove(buttonPane);
	}
	
	public void showSplitDistance(Game game, Stage owner) {
	    // Label with styled text
	    Label msg = new Label("Enter Split Distance");
	    msg.setWrapText(true);
	    msg.setMaxWidth(380);
	    msg.setTextAlignment(TextAlignment.CENTER);
	    msg.setAlignment(Pos.CENTER);
	    msg.setTextFill(Color.web("#fdf6e3")); // soft ivory
	    msg.setStyle("-fx-font-size: 18px; -fx-font-family: 'Georgia';");
	    
	    TextField distanceField = new TextField();
	    distanceField.setMaxWidth(150);
	    distanceField.setStyle(
        "-fx-background-color: rgba(255,255,255,0.7);" +
        "-fx-font-size: 18px;" +
        "-fx-font-family: 'Georgia';" +
        "-fx-text-fill: #000;"
	    );
	    distanceField.setPromptText("e.g. 5");
	    
	    Button submitButton = new Button("Submit");
	    submitButton.setStyle("-fx-background-color: #fdf6e3; -fx-text-fill: #5c3b24; -fx-font-weight: bold;");
	    
	    // VBox to hold content
	    VBox content = new VBox(msg, distanceField, submitButton);
	    content.setAlignment(Pos.CENTER);
	    content.setSpacing(15);
	
	    // Styled background rectangle
	    Rectangle background = new Rectangle(400, 200);
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
	    popup.setTitle("Split Distance");
	
	    Scene scene = new Scene(root, 400, 200);
	    popup.setScene(scene);
	
	    // Optional icon
	    Image icon = new Image("icon.png");
	    popup.getIcons().add(icon);
	
	    popup.setOnCloseRequest(evt -> popup.hide());
	    popup.show();
	    
	    submitButton.setOnAction(e -> {
	        String input = distanceField.getText().trim();
	        if (!input.isEmpty()) {
	            try {
					game.editSplitDistance(Integer.parseInt(input));
				} catch (Exception e1) {
					showPopMessage(owner, e1);
				}
	            popup.close();
	        }
	    });
	}
	
	public void updateHand(int i){
		Player player=game.getPlayers().get(i);
		PlayerHandView handView=playersHandsView.get(i);
		ArrayList<Card> hand=player.getHand();
		for(int j=0;j<handView.size();j++){
			boolean f=false;
			for(int r=0;r<hand.size();r++)
				if(hand.get(r)==handView.get(j).getCard()){
					f=true;
					break;
				}
			if(!f){
				CardView cardView=handView.get(j);
				handView.remove(cardView);
				if(i==1){
					if(hand.size()==3)
						handView.setTranslateX(-95);
					if(hand.size()==2)
						handView.setTranslateX(-30);
					if(hand.size()==1)
						handView.setTranslateX(35);
				}
				if(i==3){
					if(hand.size()==3)
						handView.setTranslateX(35);
					if(hand.size()==2)
						handView.setTranslateX(-30);
					if(hand.size()==1)
						handView.setTranslateX(-95);
					
				}

			}
		}
	}
	
     
            public void field(int i){
            	
            	HomeZoneView homeZone=homeZonesView.get(i);
            	trackView.getMainTrack().get(i*25).setMarbleView(homeZone.fieldMarble ());
            	

            }
           public void field(){
            	int i=game.getCurrentPlayerIndex();
            	HomeZoneView homeZone=homeZonesView.get(i);
            	CellView cell =homeZone.getCellView();
            	trackView.getMainTrack().get(i*25).setMarbleView(cell.getMarbleView());

            }
         
            
            
       
           public void updatePlayerHighlights(int currentPlayerIndex){
        	   for(int i=0;i<4;i++){
        		   playersView.get(i).highlightCurrentPlayer(i == currentPlayerIndex);
        		   playersView.get(i).highlightNextPlayer(i == ((currentPlayerIndex + 1) % 4));
        		   
        	   }
        	  
           }
        
      

       public void setHands(){
  	     for(int i=0;i<4;i++){
  	         ArrayList<Card>hand=game.getPlayers().get(i).getHand();
  	         playersHandsView.get(i).setHandCardsView(hand);
  	         if(i==1)playersHandsView.get(i).setTranslateX(-160);
	         if(i==3)playersHandsView.get(i).setTranslateX(100);
  	         if(i!=0)
  	        	playersHandsView.get(i).showBack();
  	         
  	     
  	     
  	     }
  	 
  	 }

}
	
	
	
	
	
	