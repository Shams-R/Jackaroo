package view;

import java.util.ArrayList;

import model.player.Player;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JackarooView {
	private String playerName;
	private StackPane mainLayout;
	private ArrayList<Circle> mainTrack;
	private ArrayList<ArrayList> safeZones;
	private ArrayList<ArrayList> homezones;
	
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
	    board.setPrefSize(1100, 1100);
	    board.setMinSize(1100, 1100);
	    board.setMaxSize(1100, 1100);
	    
	    board.setTranslateY(10);
	    
	    // Set background image for the board
	    Image backgroundImage = new Image("Board.png");
	    BackgroundSize backgroundSize = new BackgroundSize(
	        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);

	    BackgroundImage bgImage = new BackgroundImage(
	        backgroundImage,
	        BackgroundRepeat.NO_REPEAT,
	        BackgroundRepeat.NO_REPEAT,
	        BackgroundPosition.CENTER,
	        backgroundSize);

	    board.setBackground(new Background(bgImage));

	    // Add the cells to the board
	    addCells(board, players); // 👈 Add all circular cells here
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
    	
    	int x = 120;
    	int y = 398;
    	
    	for(int i=1; i<10; i++) {
    		mainTrack.add(mainTrackCell(x, y+(13*2+8)*i));
    	}
    	
    	y = (int) mainTrack.get(mainTrack.size()-1).getCenterY();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x+(13*2+8)*i, y));
    	}
    	
    	x = (int) mainTrack.get(mainTrack.size()-1).getCenterX();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x, y+(13*2+8)*i));
    	}
    	
    	y = (int) mainTrack.get(mainTrack.size()-1).getCenterY();
    	
    	for(int i=1; i<10; i++) {
    		mainTrack.add(mainTrackCell(x+(13*2+8)*i, y));
    	}
    	
    	x = (int) mainTrack.get(mainTrack.size()-1).getCenterX();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x, y-(13*2+8)*i));
    	}
    	
    	y = (int) mainTrack.get(mainTrack.size()-1).getCenterY();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x+(13*2+8)*i, y));
    	}
    	
    	x = (int) mainTrack.get(mainTrack.size()-1).getCenterX();
    	
    	for(int i=1; i<10; i++) {
    		mainTrack.add(mainTrackCell(x, y-(13*2+8)*i));
    	}
    	
    	y = (int) mainTrack.get(mainTrack.size()-1).getCenterY();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x-(13*2+8)*i, y));
    	}
    	
    	x = (int) mainTrack.get(mainTrack.size()-1).getCenterX();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x, y-(13*2+8)*i));
    	}
    	
    	y = (int) mainTrack.get(mainTrack.size()-1).getCenterY();
    	
    	for(int i=1; i<10; i++) {
    		mainTrack.add(mainTrackCell(x-(13*2+8)*i, y));
    	}
    	
    	x = (int) mainTrack.get(mainTrack.size()-1).getCenterX();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x, y+(13*2+8)*i));
    	}
    	
    	y = (int) mainTrack.get(mainTrack.size()-1).getCenterY();
    	
    	for(int i=1; i<9; i++) {
    		mainTrack.add(mainTrackCell(x-(13*2+8)*i, y));
    	}
    	
    	System.out.println(mainTrack.size());
    	
    	for(Circle cell : mainTrack) {
    		board.getChildren().add(cell);
    	}
        
    	//Base Cells 
    	
        for(int i=0; i<4; i++) {
        	int baseIndex = (i*25+100)%100;
        	mainTrack.get(baseIndex).setStroke(Color.valueOf(players.get(i).getColour().toString()));
        	mainTrack.get(baseIndex).setStrokeWidth(4);
        }
        
        //Safezone Cells
        
        safeZones = new ArrayList<>();
        
        for(int i=0; i<4; i++) {
        	int entry = (i*25+102)%100;
        	
        	Circle entryCell = mainTrack.get(entry);
        	int entryX = (int) entryCell.getCenterX();
        	int entryY = (int) entryCell.getCenterY();
        	
        	ArrayList<Circle> safeZone = new ArrayList<>();
        	String colour = players.get(i).getColour().toString();
        	
            if (i == 0) {
                for (int j = 1; j < 5; j++) {
                    safeZone.add(zoneCell(colour, entryX + j * (13 * 2 + 5), entryY));
                }
            } else if (i == 1) {
                for (int j = 1; j < 5; j++) {
                    safeZone.add(zoneCell(colour, entryX, entryY - j * (13 * 2 + 5)));
                }
            } else if (i == 2) {
                for (int j = 1; j < 5; j++) {
                    safeZone.add(zoneCell(colour, entryX - j * (13 * 2 + 5), entryY));
                }
            } else if (i == 3) {
                for (int j = 1; j < 5; j++) {
                    safeZone.add(zoneCell(colour, entryX, entryY + j * (13 * 2 + 5)));
                }
            }

            safeZones.add(safeZone);

            for (Circle cell : safeZone) {
                board.getChildren().add(cell);
            }
        }
        
        //Homezone cells
        
        homezones = new ArrayList<>();
        
        for(int i=0; i<4; i++) {
        	int startIndex = (i*25-13+100)%100;
        	int posX = (int) mainTrack.get(startIndex).getCenterX();
        	int posY = (int) mainTrack.get(startIndex).getCenterY();
        	
        	ArrayList<Circle> homeZone = new ArrayList<>();
        	
        	String colour = players.get(i).getColour().toString();
        	if(i==0) {
        		posX-=4*34+5;
        		homeZone.add(zoneCell(colour, posX, posY));
        		homeZone.add(zoneCell(colour, posX, posY+34+5));
        		homeZone.add(zoneCell(colour, posX+34+5, posY));
        		homeZone.add(zoneCell(colour, posX+34+5, posY+34+5));
        	}
        	else if(i==1) {
        		posY+=4*34+5;
        		homeZone.add(zoneCell(colour, posX, posY));
        		homeZone.add(zoneCell(colour, posX, posY-34-5));
        		homeZone.add(zoneCell(colour, posX+34+5, posY));
        		homeZone.add(zoneCell(colour, posX+34+5, posY-34-5));
        	}
        	else if(i==2) {
        		posX+=4*34+5;
        		homeZone.add(zoneCell(colour, posX, posY));
        		homeZone.add(zoneCell(colour, posX, posY-34-5));
        		homeZone.add(zoneCell(colour, posX-34-5, posY));
        		homeZone.add(zoneCell(colour, posX-34-5, posY-34-5));
        	}
        	else if(i==3) {
        		posY-=4*34+5;
        		homeZone.add(zoneCell(colour, posX, posY));
        		homeZone.add(zoneCell(colour, posX, posY+34+5));
        		homeZone.add(zoneCell(colour, posX-34-5, posY));
        		homeZone.add(zoneCell(colour, posX-34-5, posY+34+5));
        	}
        	
        	homezones.add(homeZone);
        	
        	for(Circle cell : homeZone) {
        		board.getChildren().add(cell);
        	}
        }
    }
}