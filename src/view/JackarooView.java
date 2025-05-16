package view;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import engine.board.Cell;
import engine.board.SafeZone;
import model.card.Card;
import model.player.Marble;
import model.player.Player;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JackarooView {
	private String playerName;
	private String playerGender;
	private StackPane mainLayout;
	private TrackView trackView;
	private ArrayList<SafeZoneView> safeZonesView;
	private ArrayList<PlayerHandView> playersHandsView;
	private ArrayList<HomeZoneView> homeZonesView;
	private CardsPoolView cardsPool;
	
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
        	
        	Circle entryCell = trackView.getMainTrack().get(entry).getCircle();
        	int entryX = (int) entryCell.getCenterX();
        	int entryY = (int) entryCell.getCenterY();
    		
        	SafeZoneView safeZone = new SafeZoneView(safeZones.get(i).getCells(), players.get(i).getColour().toString(), entryX, entryY, i);
    		safeZonesView.add(safeZone);
    		
            for (CellView cell : safeZone.getSafeZoneView()) {
                board.getChildren().add(cell.getCircle());
            }
    	}
    }
    
    private void addTrack(Pane board, ArrayList<Player> players, ArrayList<Cell> track) {
    	trackView = new TrackView(track, players);
    	
    	ArrayList<CellView> mainTrack = trackView.getMainTrack();
    	
    	for(CellView cell : mainTrack) {
    		board.getChildren().add(cell.getCircle());
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
    	mainLayout.getChildren().add(root);
    }
    
    public static void showPopMessage(Stage owner, Exception e) {
    	
    	Label msg = new Label(e.getMessage());
	    
	    msg.setWrapText(true);
	    msg.setMaxWidth(380);
	    msg.setTextAlignment(TextAlignment.CENTER);
	    msg.setAlignment(Pos.CENTER);
	    msg.setStyle("-fx-padding: 10;");

	    VBox root = new VBox(msg);
	    root.setAlignment(Pos.CENTER);
	    root.setFillWidth(true);

	    Stage popup = new Stage();
	    popup.initOwner(owner);
	    popup.initModality(Modality.WINDOW_MODAL);
	    popup.setResizable(false);
	    popup.setTitle("wrong move");
	    Scene scene = new Scene(root, 300, 150);
	    popup.setScene( scene);

	    popup.setOnCloseRequest(evt -> popup.hide());
	    popup.show();
	    
	    Image icon=new Image("icon.png");
		popup.getIcons().add(icon);
	}
}