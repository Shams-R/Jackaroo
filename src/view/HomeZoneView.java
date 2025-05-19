package view;

import java.util.ArrayList;

import model.player.Marble;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class HomeZoneView extends StackPane{
	private final GridPane grid;
	private final ArrayList<CellView> cells;
	private int numberOfMarbles;


	
	
	public HomeZoneView(ArrayList<CellView> cells){
		numberOfMarbles=4;
		this.cells = cells;
		grid=new GridPane();
		grid.setHgap(5); // Horizontal gap between cells
		grid.setVgap(5); // Vertical gap between cells
		for (int i = 0; i < 4; i++) {
		    int row = i / 2; // 0 or 1
		    int col = i % 2; // 0 or 1
		    grid.add(cells.get(i),col, row); // Add to grid at (col, row)
		}
		
		getChildren().add(grid);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	}
	
	public GridPane getGrid() {
		return grid;
	}
	
	public ArrayList<CellView> getCells() {
		return cells;
	}
	public MarbleView fieldMarble (){
		CellView cell=cells.get(--numberOfMarbles);
		MarbleView marble=cell.getMarbleView();
		return marble;
		
	}
	public CellView getCellView(){
		return cells.get(--numberOfMarbles);
	}

	public Object getColour() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumberOfMarbles() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void set(ArrayList<Marble>marbles){
		while(numberOfMarbles>0){
			cells.get(numberOfMarbles-1).getMarbleView();
			numberOfMarbles--;
		}
		for(int i=0;i<marbles.size();i++)
		{
			MarbleView marbleView=new MarbleView(marbles.get(i));
			cells.get(numberOfMarbles).setMarbleView(marbleView);
			numberOfMarbles++;
		}
		
	/*	if(numberOfMarbles==marbles.size())return;
		if(numberOfMarbles>marbles.size()){
			cells.get(numberOfMarbles-1).getMarbleView();
			numberOfMarbles--;
		}
		else{
			MarbleView marble=new MarbleView(marbles.get(marbles.size()-1));
			cells.get(numberOfMarbles).setMarbleView(marble);
		}*/
		
		
	}
	
	
	public void add(MarbleView marbleView){
		cells.get(numberOfMarbles).setMarbleView(marbleView);
		numberOfMarbles++;
	}
}
