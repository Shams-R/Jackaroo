package view;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import engine.board.Cell;

public class CellView extends StackPane{
	private final Cell cell;
	private final Circle circle;
	private MarbleView marbleView;
	
	public CellView (String colour){
		this.cell=null;
	    circle = new Circle();
    	circle.setRadius(13);
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.valueOf(colour));
        circle.setStrokeWidth(4);
        getChildren().add(circle);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	}
	public CellView (Cell cell){
		this.cell=cell;
	    circle = new Circle();
    	circle.setRadius(13);
    	circle.setFill(Color.BEIGE);
        circle.setStroke(Color.BLACK);
        getChildren().add(circle);
	}
	public MarbleView getMarbleView() {
		getChildren().remove(marbleView);
		return marbleView;
	}
	public boolean isFull(){
		return marbleView!=null;
	}
	public void setMarbleView(MarbleView marbleView) {
		this.marbleView=marbleView;
		getChildren().add(marbleView);
	}
	
	public Cell getCell() {
		return cell;
	}
	public Circle getCircle() {
		return circle;
	}
	
	
}
