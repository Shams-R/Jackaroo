package view;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import controller.JackarooGUI;
import engine.Game;
import engine.board.Cell;
import engine.board.SafeZone;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.SplitOutOfRangeException;
import model.Colour;
import model.card.Card;
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
	private BorderPane splitDistancePane;
	private Game game;
	
	
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

	    for (int i = 0; i < 4; i++) {
	        Player player = players.get(i);

	        // Create PlayerView
	        PlayerView playerView = new PlayerView(player, i == 0 ? getPlayerGender() : "");
	        playerView.setPrefSize(100, 100);

	        // Create label for the name
	        Label nameLabel = new Label(player.getName());
	        nameLabel.setStyle("-fx-text-fill: #5C3317; " +
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
	        "-fx-background-color: #f5deb3;" +         // Light wood tone
	        "-fx-text-fill: #5C3317;" +                // Dark wooden text
	        "-fx-font-family: 'Georgia';" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10;" +
	        "-fx-border-color: saddlebrown;" +         // Wooden border
	        "-fx-border-width: 4;" +
	        "-fx-border-radius: 10;"
	    );

	    playButton.setOnAction(e -> {
	        CardView card = JackarooGUI.getCurrentlySelectedCard();
	        ArrayList<MarbleView> marbles = JackarooGUI.getSelectedMarbles();
	        act(card, marbles);
	    });

	    buttonPane = new Pane();
	    buttonPane.setPickOnBounds(false);
	    buttonPane.getChildren().add(playButton);

	    playButton.setTranslateY(500);
	    playButton.setTranslateX(100);
	    playButton.setPrefSize(180, 80);

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

	    mainLayout.getChildren().add(buttonPane);
	}

	
	public void removePlayButton() {
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
	public void act(CardView cardView ,ArrayList<MarbleView> selectedMarbles){
		Card card=cardView.getCard();
		if(card instanceof Standard ){
			Standard card2=(Standard)card;
			if(selectedMarbles.size()==1 && ! (card instanceof Four) && ! (card instanceof Five)){
				
				move(selectedMarbles.get(0),card2.getRank(),true);
			}
			else{
				if(card instanceof Ace || card instanceof King){
					field();
				}
				if(card instanceof Seven){
				    move(selectedMarbles.get(0),this.game.getBoard().getSplitDistance(),true);
					move(selectedMarbles.get(1),7-this.game.getBoard().getSplitDistance(),true);
					
				}
				if(card instanceof Four)
				   move(selectedMarbles.get(0),-(card2.getRank()));
				if(card instanceof Five){
					MarbleView marble=selectedMarbles.get(0);
					int i=game.getCurrentPlayerIndex();
					ArrayList<Player> players=game.getPlayers();
					Player player=players.get(i); 
					boolean f= (marble.getColour()==player.getColour());
					move(marble,5,f);
				}
				
				if(card instanceof Jack)
				swap(selectedMarbles.get(0),selectedMarbles.get(1));
				if(card instanceof Ten)
				{
					int i=game.getCurrentPlayerIndex();
					i=(i+1)%4;
					updateHand(i);
					
				}
					
				if(card instanceof Queen){
					int j=game.getCurrentPlayerIndex();
					for(int i=0;i<4;i++)
						if(j!=i){
							updateHand(i);
						}
					
				}
					
				
			}
		}
		else{
			if(card instanceof Burner);
				//burn(selectedMarbles.get(0));
			else;
				//save(selectedMarbles.get(0));
		}
	}
	public int getEntry(Colour colour){
		int i=0;
		ArrayList<Player> players=game.getPlayers();
		for(int j=0;j<4;j++)
			if(players.get(j).getColour()==colour){
				i=j;
				break;
			}
		return (i*25+99)%100;
			
		
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
				// Step 1: Get current position of cardView in scene
			    Bounds cardBoundsInScene = cardView.localToScene(cardView.getBoundsInLocal());

			    // Step 2: Convert to mainLayout coordinates
			    Bounds cardBoundsInLayout = mainLayout.sceneToLocal(cardBoundsInScene);

			    // Step 3: Remove from handView and add to mainLayout
			    handView.remove(cardView);
			    mainLayout.getChildren().add(cardView);

			    // Step 4: Set its position in mainLayout based on where it was
			    cardView.relocate(cardBoundsInLayout.getMinX(), cardBoundsInLayout.getMinY());

			    // Step 5: Get firePit position in mainLayout
			    Bounds firePitBounds = firePit.localToScene(firePit.getBoundsInLocal());
			    Bounds firePitInLayout = mainLayout.sceneToLocal(firePitBounds);

			    double targetX = firePitInLayout.getMinX();
			    double targetY = firePitInLayout.getMinY();

			    // Step 6: Animate movement from current position to firePit
			    TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), cardView);
			    tt.setToX(targetX - cardBoundsInLayout.getMinX());
			    tt.setToY(targetY - cardBoundsInLayout.getMinY());

			    // Step 7: Cleanup after animation
			    tt.setOnFinished(event -> {
			        cardView.setTranslateX(0);
			        cardView.setTranslateY(0);
			        cardView.relocate(targetX, targetY);
			        firePit.add(cardView); // Now officially place it in the firepit
			        mainLayout.getChildren().remove(cardView);
			    });

			    tt.play();
			    break;
			}
		}
		
		
	}
	public int getPosition(MarbleView marble,ArrayList<CellView>path){
		for(int i=0;i<path.size();i++)
			if(marble==path.get(i).getWithOutRemove())
				return i;
		return -1;
		
	}
	
    public void swap(MarbleView marble1, MarbleView marble2){
    	ArrayList<CellView>path=trackView.getMainTrack();
		int pos1=getPosition(marble1,path);
		int pos2=getPosition(marble2,path);
		ArrayList<CellView>track=trackView.getMainTrack();
		CellView cell1=track.get(pos1);
		CellView cell2=track.get(pos2);
		
		swapWithArcAnimation(marble1,marble2,cell1,cell2);
		
	}
	public void swapWithArcAnimation(MarbleView marble1, MarbleView marble2,CellView cell1,CellView cell2) {
	   

	    double x1 = cell1.getX();
	    double y1 = cell1.getY();
	    double x2 = cell2.getX();
	    double y2 = cell2.getY();

	    // Detach from original cells and add to overlay pane
	    cell1.getMarbleView();
	    cell2.getMarbleView();
	    mainLayout.getChildren().addAll(marble1, marble2);

	    marble1.setLayoutX(x1);
	    marble1.setLayoutY(y1);
	    marble2.setLayoutX(x2);
	    marble2.setLayoutY(y2);

	    // Create arc paths
	    Path path1 = createArcPath(x1, y1, x2, y2);
	    Path path2 = createArcPath(x2, y2, x1, y1);

	    PathTransition transition1 = new PathTransition(Duration.millis(700), path1, marble1);
	    PathTransition transition2 = new PathTransition(Duration.millis(700), path2, marble2);

	    transition1.setOrientation(PathTransition.OrientationType.NONE);
	    transition2.setOrientation(PathTransition.OrientationType.NONE);

	    transition1.setOnFinished(e -> {
	        mainLayout.getChildren().remove(marble1);
	        cell2.setMarbleView(marble1);
	    });

	    transition2.setOnFinished(e -> {
	        mainLayout.getChildren().remove(marble2);
	        cell1.setMarbleView(marble2);
	    });

	    transition1.play();
	    transition2.play();
	}

	private Path createArcPath(double startX, double startY, double endX, double endY) {
	    Path path = new Path();
	    path.getElements().add(new MoveTo(startX, startY));

	    double controlX = (startX + endX) / 2;
	    double controlY = Math.min(startY, endY) - 50; // control point above to create an arc

	    QuadCurveTo curve = new QuadCurveTo(controlX, controlY, endX, endY);
	    path.getElements().add(curve);
	    return path;
	}
	public void destroy(MarbleView marbleView) {
		Pane overlayPane = new Pane();
		mainLayout.getChildren().add(overlayPane);
	    int index = getPositionInTrackView(marbleView);
	    ArrayList<CellView> mainTrack = trackView.getMainTrack();
	    mainTrack.get(index).setMarbleView(null);

	    Colour colour = marbleView.getMarble().getColour();
	    ArrayList<CellView> homeZoneView = getHomeZoneView(colour).getCells();

	    int row = getHomeZoneView(colour).getNumberOfMarbles() / 2;
	    int col = getHomeZoneView(colour).getNumberOfMarbles() % 2;
	    CellView finalCell=null;
	    for(CellView cellView: homeZoneView){
	    	if(cellView.getMarbleView()==null){
	    		finalCell=cellView;
	    		break;
	    	}
	    		
	    }

	    // Get scene coordinates before moving
	    Bounds startBounds = marbleView.localToScene(marbleView.getBoundsInLocal());

	    // Add to overlay pane (assume you have a transparent Pane above everything)

	    overlayPane.getChildren().add(marbleView);

	    // Set position on overlay
	    marbleView.setLayoutX(startBounds.getMinX());
	    marbleView.setLayoutY(startBounds.getMinY());

	    // Get destination bounds
	 
	    Bounds endBounds = finalCell.localToScene(finalCell.getBoundsInLocal());

	    // Create animation
	    TranslateTransition transition = new TranslateTransition(Duration.seconds(1), marbleView);
	    transition.setToX(endBounds.getMinX() - startBounds.getMinX());
	    transition.setToY(endBounds.getMinY() - startBounds.getMinY());

	    // Sound
	    AudioClip boomSound = new AudioClip(getClass().getResource("/sounds/boom.wav").toExternalForm());
	    boomSound.play();

	    transition.setOnFinished(e -> {
	    	overlayPane.getChildren().remove(marbleView);
	    	mainLayout.getChildren().remove(overlayPane);
	        marbleView.setTranslateX(0);
	        marbleView.setTranslateY(0);
	        marbleView.setLayoutX(0);
	        marbleView.setLayoutY(0);
	        getHomeZoneView(colour).getGrid().add(marbleView, col, row);
	    });

	    transition.play();
	}



	public int getPositionInTrackView(MarbleView marbleView ){
		ArrayList<CellView> mainTrack=trackView.getMainTrack();
		for(int i=0; i<trackView.getMainTrack().size();i++){
			
			if(mainTrack.get(i).getMarbleView().equals(marbleView)){
				return i;
			}
		}
		return -1;
	
	}
	
	public HomeZoneView getHomeZoneView(Colour colour){
		for(HomeZoneView homeZoneView: homeZonesView){
			if(homeZoneView.getColour().equals(colour))
				return homeZoneView;
		}
		return null;
	}
	
	public void save(MarbleView marbleView, Game game) {
	    ArrayList<CellView> safeZoneView = getSafeZoneView(marbleView).getSafeZoneView();
	    ArrayList<Cell> safeZone = getSafeZone(marbleView, game).getCells();

	    for (int i = 0; i < 4; i++) {
	        if (safeZone.get(i).getMarble() == marbleView.getMarble()) {
	        	int index=i;
	            // Get the scene position of marbleView and target safeZoneView
	            Bounds startBounds = marbleView.localToScene(marbleView.getBoundsInLocal());
	            Bounds endBounds = safeZoneView.get(i).localToScene(safeZoneView.get(i).getBoundsInLocal());

	            double xStart = startBounds.getMinX();
	            double yStart = startBounds.getMinY();
	            double xEnd = endBounds.getMinX();
	            double yEnd = endBounds.getMinY();

	            Parent originalParent = marbleView.getParent();

	            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), marbleView);
	            transition.setToX(xEnd - xStart);
	            transition.setToY(yEnd - yStart);

	            transition.setOnFinished(e -> {
	                // Clean up the old location
	                if (originalParent instanceof Pane) {
	                    ((Pane) originalParent).getChildren().remove(marbleView);
	                }

	                
	                marbleView.setTranslateX(0);
	                marbleView.setTranslateY(0);

	                // Let the target cell add it in the correct place
	                safeZoneView.get(index).setMarbleView(marbleView);
	            });

	            transition.play();
	            break; 
	        }
	    }
	}
	
	public SafeZoneView getSafeZoneView(MarbleView marbleView){
		for (SafeZoneView safeZoneView: safeZonesView){
			if(safeZoneView.getColour().equals(marbleView.getColour().toString())){
				return safeZoneView;
			}
		}
		return null; 
	}
	public SafeZone getSafeZone(MarbleView marbleView,Game game){
		for (SafeZone safeZone:game.getBoard().getSafeZones()){
			if(safeZone.getColour().equals(marbleView.getColour())){
				return safeZone;
			}
		}
		return null;
	}
	
	public void updateSafeZone(){
		
	}
	
	
	
	
	
	
	
	public void move (MarbleView marble, int steps){
		int i=0;
	if (steps >0 ){
		 while (i < steps){
			int pos = getPosition(marble,trackView.getMainTrack() );
			
			if ( isMarbleAtSafezoneEntry(marble,trackView.getMainTrack() , game.getPlayers() ) )
				break ;
			
			animateMarbleMovement(trackView.getMainTrack().get(pos+1), trackView.getMainTrack().get(pos) );
			trackView.getMainTrack().get(pos+1).setMarbleView(trackView.getMainTrack().get(pos).getMarbleView()) ;
			
			i++;
		}
			}
		else {
			int absSteps= Math.abs(steps) ;
			while (i < absSteps){
				int pos= getPosition(marble,trackView.getMainTrack() );
				if ( isMarbleAtSafezoneEntry(marble,trackView.getMainTrack() , game.getPlayers() ) )
					break ;
				animateMarbleMovement(trackView.getMainTrack().get((pos==0)? 99: pos-1 ), trackView.getMainTrack().get(pos) );
				trackView.getMainTrack().get( (pos==0)? 99: pos-1 ).setMarbleView(trackView.getMainTrack().get(pos).getMarbleView()) ;
				i++;
			}
		}
	
	}
	
	

	public int getPlayerSafezoneIndex(ArrayList<Player> players, MarbleView marble) {
	    
	    Colour marbleColour = marble.getMarble().getColour();

	    
	    boolean playerFound = false;
	    int i=0;
	    while (i < players.size()) {
	        if (players.get(i).getColour().equals(marbleColour) ) {
	            playerFound = true;
	            break;
	        }
	        i++;
	    }
	    if (!playerFound) {
	        return -1;
	    }
	    
	    return (i==0)? i*25+98 : i*25+98-100 ;
	}
	
	


    public boolean isMarbleAtSafezoneEntry(MarbleView marble, ArrayList<CellView> path, ArrayList<Player> players) {
        int currentPosition = getPosition(marble, path);
        if (currentPosition == -1) {
            return false; 
        }
        
        int safezoneIndex = getPlayerSafezoneIndex(players, marble);
        if (safezoneIndex == -1) {
            return false; 
        }
        
        return currentPosition == safezoneIndex;
    }

 

       
        public void move (MarbleView marble, int steps, boolean safeZoneAccess ){
    		int i=0;
    	if (steps >0 ){
    		int pos = getPosition(marble,trackView.getMainTrack() ); 
    		int entry = getEntry( marble.getColour() );
    		 SafeZoneView safeZone = getSafeZoneView(marble) ;
    		while (i < steps){
    			
    			if ( (pos == entry) && (safeZoneAccess) ){
    				
    				animateMarbleMovement(trackView.getMainTrack().get(pos) , safeZone.getCellView(0) );
    				safeZone.getCellView(0).setMarbleView(trackView.getMainTrack().get(pos).getMarbleView()) ;
    				getSafeZoneView(marble); 
    				break ;
    		}
    			
    			
    			animateMarbleMovement(trackView.getMainTrack().get( (pos) ), trackView.getMainTrack().get( (pos+1)%100 ) );
    			trackView.getMainTrack().get((pos+1) %100 ).setMarbleView(trackView.getMainTrack().get(pos).getMarbleView()) ;
    			
    			pos++ ;
    			i++;
    		}
    			
    		
    		pos=0;
    		while ( (i<steps) && (pos<3) ) {
    			CellView safeCell_1 =safeZone.getCellView(pos) ;
    			CellView safeCell_2 =safeZone.getCellView(pos+1) ;
    			
    			safeCell_1.setMarbleView( safeCell_1.getMarbleView() );
    			pos++;
    			i++;
    			}
    		
    			}
    	
    		else {
    			int absSteps= Math.abs(steps) ;
    			int pos= getPosition(marble,trackView.getMainTrack() );
    			while (i < absSteps){
    				
    				
    			animateMarbleMovement(trackView.getMainTrack().get( pos ), trackView.getMainTrack().get( (pos+99)%100 ) );
    			trackView.getMainTrack().get((pos+99)%100).setMarbleView(trackView.getMainTrack().get(pos).getMarbleView()) ;
    			pos = (pos+99)%100 ;
    			i++;
    			}
    		
    		}
    	
    	}

     

           
            public void animateMarbleMovement(CellView source, CellView target ) {
                
                MarbleView marble = source.getWithOutRemove();
               
                Scene scene = mainLayout.getScene();


                
                double deltaX = (target.getX() - source.getX());
                double deltaY = (target.getY() - source.getY());

               
                TranslateTransition transition = new TranslateTransition(Duration.millis(1000), marble);
                transition.setByX(deltaX);
                transition.setByY(deltaY);
                transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

         
                transition.play();
            }
            
            public void field( ){
            	int i=0 ;//game.getCurrentPlayerIndex();
            	HomeZoneView homeZone=homeZonesView.get(i);
            	CellView cell =homeZone.getCellView();
            	
            	
            	animateField(cell, trackView.getMainTrack().get(i*25) );
            	
            	
            	
     		   
     	   }
            public void animateField( CellView sourceCell, CellView targetCell) {
            	MarbleView marble=sourceCell.getWithOutRemove();
                // 1. Get the marble’s current position in scene coordinates
                Bounds startBounds = marble.localToScene(marble.getBoundsInLocal());
                Bounds mainBounds = mainLayout.localToScene(mainLayout.getBoundsInLocal());

                double startX = startBounds.getMinX() - mainBounds.getMinX();
                double startY = startBounds.getMinY() - mainBounds.getMinY();

                // 2. Get target position in scene coordinates
                Bounds targetBounds = targetCell.localToScene(targetCell.getBoundsInLocal());
                double endX = targetBounds.getMinX() - mainBounds.getMinX();
                double endY = targetBounds.getMinY() - mainBounds.getMinY();

                // 3. Remove marble from source cell (but not from scene)
                sourceCell.getMarbleView();

                // 4. Temporarily add marble to the main layout overlay
                mainLayout.getChildren().add(marble);
                marble.setTranslateX(startX);
                marble.setTranslateY(startY);

                // 5. Animate movement
                TranslateTransition transition = new TranslateTransition(Duration.millis(500), marble);
                transition.setToX(endX);
                transition.setToY(endY);
                transition.setOnFinished(e -> {
                    // 6. After animation, remove from mainLayout and add to target cell
                    mainLayout.getChildren().remove(marble);
                    targetCell.setMarbleView(marble);
                });

                transition.play();
            }
        
        public void showCurrentPlayerTurn(Game game, ArrayList<PlayerView> playerViews) {
            int currentIndex = game.getCurrentPlayerIndex();
            PlayerView currentPlayerView = playerViews.get(currentIndex);

            // Highlight the current player
            Platform.runLater(() -> currentPlayerView.highlightCurrentPlayer(true));

            // Start a background thread to wait for the turn to change
            new Thread(() -> {
                while (game.getCurrentPlayerIndex() == currentIndex) {
                    try {
                        Thread.sleep(100); // avoid busy waiting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Remove highlight once turn changes
                Platform.runLater(() -> currentPlayerView.highlightCurrentPlayer(false));
            }).start();
        }
        public void showNextPlayerTurn(Game game, ArrayList<PlayerView> playerViews) {
            int nextIndex = (game.getCurrentPlayerIndex() + 1) % playerViews.size();
            PlayerView nextPlayerView = playerViews.get(nextIndex);

            Platform.runLater(() -> nextPlayerView.highlightNextPlayer(true));

            new Thread(() -> {
                while ((game.getCurrentPlayerIndex() + 1) % playerViews.size() == nextIndex) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(() -> nextPlayerView.highlightNextPlayer(false)); 
            }).start();
        } 
        
}
	
	
	
	
	
	