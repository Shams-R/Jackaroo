package view;

import java.util.ArrayList;

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
import javafx.scene.control.TextField;
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
	private StackPane mainLayout;
	private TrackView trackView;
	private ArrayList<SafeZoneView> safeZonesView;
	private ArrayList<PlayerHandView> playersHandView;
	private ArrayList<HomeZoneView> homeZonesView;
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String name) {
		playerName = name;
	}
	
	public StackPane getMainLayout() {
		return mainLayout;
	}

	public Button onGameStart(Stage stage, TextField nameField) {
	    // Main container
	    mainLayout = new StackPane();

	    // Background image view setup
	    Image background = new Image("Start.jpg");
	    ImageView view = new ImageView(background);
	    view.setPreserveRatio(false); // Don't preserve aspect ratio
	    view.setFitWidth(Screen.getPrimary().getBounds().getWidth());
	    view.setFitHeight(Screen.getPrimary().getBounds().getHeight());

	    // Semi-transparent black background rectangle
	    Rectangle r = new Rectangle();
	    r.setWidth(400);  // Adjust to your desired width
	    r.setHeight(200); // Adjust to your desired height
	    r.setArcWidth(30); // Rounded corners for aesthetic
	    r.setArcHeight(30);
	    r.setFill(Color.rgb(0, 0, 0, 0.6)); // 60% transparent black

	    // VBox to hold label, input field, and button
	    VBox userInput = new VBox(20); // 20px spacing
	    userInput.setAlignment(Pos.CENTER);

	    Label text = new Label("Enter Your Name:");
	    text.setTextFill(Color.WHITE);
	    text.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

	    nameField.setMaxWidth(250);
	    nameField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-font-size: 18px;");

	    Button submitButton = new Button("Start");
	    submitButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");

	    userInput.getChildren().addAll(text, nameField, submitButton);

	    // A container for the input and rectangle background
	    StackPane inputContainer = new StackPane();
	    inputContainer.getChildren().addAll(r, userInput);
	    inputContainer.setAlignment(Pos.CENTER);

	    // Add background image and UI to the main StackPane
	    mainLayout.getChildren().addAll(view, inputContainer);
	    StackPane.setAlignment(inputContainer, Pos.CENTER);

	    // Create and set the scene
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
	    board.setPrefSize(1115, 1115);
	    board.setMinSize(1115, 1115);
	    board.setMaxSize(1115, 1115);
	    
	    // Set background image for the board
	    Image boardImg = new Image("Board.png");
	    ImageView boardBg = new ImageView(boardImg);
	    boardBg.setPreserveRatio(false);
	    boardBg.setFitWidth(1115);
	    boardBg.setFitHeight(1115);
	    
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
    	
    	System.out.println(mainTrack.size());
    }
    
    public void makeHandsView(ArrayList<Player>players){
    	ArrayList<Card>array1=players.get(0).getHand();//1---humanplyer
    	ArrayList<Card>array2=players.get(1).getHand();
    	ArrayList<Card>array3=players.get(2).getHand();
    	ArrayList<Card>array4=players.get(3).getHand();
  
         PlayerHandView bottomPlayer = new PlayerHandView(array1);
         PlayerHandView rightPlayer  = new PlayerHandView(array2);
         PlayerHandView topPlayer    = new PlayerHandView(array3);
         PlayerHandView leftPlayer   = new PlayerHandView(array4);

         // Orient cards correctly
         leftPlayer.setRotate(90);
         rightPlayer.setRotate(-90);
         topPlayer.setRotate(180);
         BorderPane root = new BorderPane();
         
         root.setBottom(bottomPlayer);
		 BorderPane.setAlignment(bottomPlayer, Pos.CENTER);
		 bottomPlayer.setTranslateX(-35);
		 bottomPlayer.setTranslateY(-10);
		 
		 
		 root.setRight(rightPlayer);
		 BorderPane.setAlignment(rightPlayer, Pos.CENTER);
		 rightPlayer.setTranslateX(-320);
		 
		 root.setTop(topPlayer);
		 BorderPane.setAlignment(topPlayer, Pos.CENTER);
		 topPlayer.setTranslateX(-35);
		 topPlayer.setTranslateY(10);
		 
		 root.setLeft(leftPlayer);
		 BorderPane.setAlignment(leftPlayer, Pos.CENTER);
		 leftPlayer.setTranslateX(270);
         
         mainLayout.getChildren().add(root);    	
    }
    
   public void addHomeZones(ArrayList<Player>players){
	   homeZonesView = new ArrayList<>();
	   BorderPane root = new BorderPane();
	   root.setPrefSize(800,800); 
	   root.setMaxSize(800,800);
	   
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
    			homeZoneView.setTranslateY(-45);
    			
    			
    		}
    		if(i==1){
    			root.setRight(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-65);
    			}
    		
    		if(i==2){
    			root.setTop(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(-30);
    			homeZoneView.setTranslateY(45);
    		}
    		
    		if(i==3){
    			root.setLeft(homeZoneView);
    			BorderPane.setAlignment(homeZoneView, Pos.CENTER);
    			homeZoneView.setTranslateX(3);
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