package view;

import controller.JackarooGUI;
import model.Colour;
import model.player.Marble;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import engine.board.Cell;
import engine.board.CellType;

public class CellView extends StackPane{
	private final Cell cell;
	private final Circle circle;
	private MarbleView marbleView;
	private double x;
	private double y;
	
	
	public CellView (String colour){ //Homezone cells
		this.cell = null;
	    circle = new Circle();
    	circle.setRadius(13);
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.valueOf(colour));
        circle.setStrokeWidth(4);
        getChildren().add(circle);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
		 setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(marbleView!=null)
						JackarooGUI.selectMarble(marbleView, CellView.this);
				}
      });
	}
	
	public CellView (String colour, Cell cell, int x, int y){//Safezone cells
		this.x=x;
		this.y=y;
		this.cell = cell;
	    circle = new Circle();
    	circle.setRadius(13);
    	setTranslateX(x);
    	setTranslateY(y);
    	//circle.setCenterX(x);
    	//circle.setCenterY(y);
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.valueOf(colour));
        circle.setStrokeWidth(4);
        getChildren().add(circle);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
		 setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(marbleView!=null)
						JackarooGUI.selectMarble(marbleView, CellView.this);
				}
   });
	}
	
	public CellView (Cell cell, int x, int y){ //Main track cells
		this.x=x;
		this.y=y;
		this.cell=cell;
	    circle = new Circle();
    	circle.setRadius(13);
    	setLayoutX(x);
    	setLayoutY(y);
    	//setTranslateX(x);
    	//setTranslateY(y);
    	//circle.setCenterX(x);
    	//circle.setCenterY(y);
    	
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.BLACK);
        getChildren().add(circle);
       
        
        
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
		 setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(marbleView!=null)
						JackarooGUI.selectMarble(marbleView, CellView.this);
				}
   });
	}
	
	public MarbleView getMarbleView() {
		getChildren().remove(marbleView);
		requestLayout();
		MarbleView m=marbleView;
		marbleView=null;
		
		circle.setFill(Color.BEIGE);
		
		return m;
	}
	
	public void setMarbleView(MarbleView marbleView) {
	    if (this.marbleView != null) {
	        getChildren().remove(this.marbleView);  // Remove previous marble
	    }
	    
	    this.marbleView = marbleView;
	    
	    if (marbleView != null) {
	         // Add new marble
	    	ImagePattern marbleImage = new ImagePattern(marbleView.getImageView().getImage());
	        circle.setFill(marbleImage);
	        marbleView.toFront();  // Ensure visibility on top
	    }
	    
	    
	    
	    requestLayout();  // Force UI update
	}
	public MarbleView getWithOutRemove(){
		return marbleView;
	}

	public boolean isFull(){
		return marbleView!=null;
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public Circle getCircle() {
		return circle;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
