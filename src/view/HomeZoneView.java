package view;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class HomeZoneView extends StackPane{
	private final GridPane grid;
	private final ArrayList<CellView> cells;
	
	public HomeZoneView(ArrayList<CellView> cells){
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
}
