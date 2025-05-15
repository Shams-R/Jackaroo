package view;

import java.util.ArrayList;

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
	private ArrayList<Circle> mainTrack;
	private ArrayList<ArrayList> safeZones;
	private ArrayList<ArrayList> homezones;
	private ArrayList<PlayerHandView>playersHandView;
	private ArrayList<HomeZoneView>homeZonesView;
	private ArrayList<PlayerHandView>playersHands;
	private CardsPoolView cardsPool;
	
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
	
	public void initializeBoard(Stage stage, ArrayList<Player> players) {
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

	    // Add it *behind* your cells:
	    board.getChildren().add(0, boardBg);

	    // Now lay down your track cells on top
	    addCells(board, players);
	}
	
    private Circle mainTrackCell(int x, int y) {
    	Circle circle = new Circle();
    	circle.setCenterX(x);
    	circle.setCenterY(y);
    	circle.setRadius(13);
//    	Image cell = new Image("Cell.png", 50, 50, true, true);
//    	ImagePattern cellPattern = new ImagePattern(cell);
//    	circle.setFill(cellPattern);
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.BLACK);
        return circle;
    }
    
    private Circle zoneCell(String colour, int x, int y) {
    	Circle circle = new Circle();
    	circle.setCenterX(x);
    	circle.setCenterY(y);
    	circle.setRadius(13);
//    	Image cell = new Image("Cell.png", 50, 50, true, true);
//    	ImagePattern cellPattern = new ImagePattern(cell);
//    	circle.setFill(cellPattern);
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.valueOf(colour));
        circle.setStrokeWidth(4);
        return circle;
    }


    private void addCells(Pane board, ArrayList<Player> players) {
    	mainTrack = new ArrayList<>();
    	
    	//reference points
    	
    	int x = 159;
    	int y = 300;
    	
    	for(int i=0; i<7; i++) {
    		x+=23;
    		y-=22;
    	}
    	
    	x-=23;
    	y+=22;
    	
    	mainTrack.add(mainTrackCell(x, y));
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y+=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=32;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y-=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<7; i++) {
    		x+=23;
    		y+=22;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y+=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		y+=32;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y+=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<7; i++) {
    		x-=23;
    		y+=22;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y-=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=32;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y+=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<7; i++) {
    		x-=23;
    		y-=22;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y-=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		y-=32;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y-=23;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	for(int i=0; i<6; i++) {
    		x+=23;
    		y-=22;
    		mainTrack.add(mainTrackCell(x, y));
    	}
    	
    	for(Circle cell : mainTrack) {
    		board.getChildren().add(cell);
    	}
    	
    	//Base Cells 
    	
        for(int i=0; i<4; i++) {
        	int baseIndex = (i*25+100)%100;
        	mainTrack.get(baseIndex).setStroke(Color.valueOf(players.get(i).getColour().toString()));
        	mainTrack.get(baseIndex).setStrokeWidth(4);
        }
    	
    	System.out.println(mainTrack.size());
    	    
        //Safezone Cells
        
        safeZones = new ArrayList<>();
        
        
        for(int i=0; i<4; i++) {
        	int entry = (i*25+98)%100;
        	
        	Circle entryCell = mainTrack.get(entry);
        	int entryX = (int) entryCell.getCenterX();
        	int entryY = (int) entryCell.getCenterY();
        	
        	ArrayList<Circle> safeZone = new ArrayList<>();
        	String colour = players.get(i).getColour().toString();
        	
            if (i == 0) {
            	for(int c=0; c<4; c++) {
            		entryX+=22;
            		entryY+=23;
            		safeZone.add(zoneCell(colour, entryX, entryY));
            	}
            } else if (i == 1) {
            	for(int c=0; c<4; c++) {
            		entryX-=22;
            		entryY+=23;
            		safeZone.add(zoneCell(colour, entryX, entryY));
            	}
            } else if (i == 2) {
            	for(int c=0; c<4; c++) {
            		entryX-=22;
            		entryY-=23;
            		safeZone.add(zoneCell(colour, entryX, entryY));
            	}
            } else if (i == 3) {
            	for(int c=0; c<4; c++) {
            		entryX+=22;
            		entryY-=23;
            		safeZone.add(zoneCell(colour, entryX, entryY));
            	}
            }

            safeZones.add(safeZone);

            for (Circle cell : safeZone) {
                board.getChildren().add(cell);
            }
        }
        
    }
    public void makeHandsView(ArrayList<Player>players){
    	playersHands=new ArrayList<>();
    	for(int i=0;i<4;i++){
    		ArrayList<Card>array=players.get(i).getHand();
    		PlayerHandView playerHand = new PlayerHandView(array);
    		playersHands.add(playerHand);
    	}
  
         PlayerHandView bottomPlayer = playersHands.get(0);
         PlayerHandView rightPlayer  = playersHands.get(1);
         PlayerHandView topPlayer    = playersHands.get(2);
         PlayerHandView leftPlayer   = playersHands.get(3);
         for(int i=1;i<4;i++){//to showBack of oponent cards
        	 PlayerHandView hand=playersHands.get(i);
        	 hand.showBack();
         }
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
   public void createHomeZones(ArrayList<Player>players){
	   homeZonesView=new ArrayList<>();
	   BorderPane root = new BorderPane();
	   root.setPrefSize(800,800); 
	   root.setMaxSize(800,800);
    	for(int i=0;i<4;i++){
    		ArrayList<Marble>marbles=players.get(i).getMarbles();
    		ArrayList<CellView>cellsview=new ArrayList<>();
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
   public void createCardsPool(int numberOfCards){
	   
	   cardsPool=new CardsPoolView(numberOfCards);
	   mainLayout.getChildren().add(cardsPool);
	   cardsPool.setTranslateX(120);
	   
	   
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