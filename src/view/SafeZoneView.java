package view;

import java.util.ArrayList;

import engine.board.Cell;

public class SafeZoneView {
	private ArrayList<CellView> safeZoneView;
	private String colour;
	
	public SafeZoneView(ArrayList<Cell> safeZoneCells ,String colour, int entryX, int entryY, int index) {
		safeZoneView = new ArrayList<CellView>();
		this.colour=colour;
            if (index == 2) {
            	for(int c=0; c<4; c++) {
            		entryX+=22;
            		entryY+=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            } else if (index == 3) {
            	for(int c=0; c<4; c++) {
            		entryX-=22;
            		entryY+=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            } else if (index == 0) {
            	for(int c=0; c<4; c++) {
            		entryX-=22;
            		entryY-=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            } else if (index == 1) {
            	for(int c=0; c<4; c++) {
            		entryX+=22;
            		entryY-=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            }
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public void setSafeZoneView(ArrayList<CellView> safeZoneView) {
		this.safeZoneView = safeZoneView;
	}

	public ArrayList<CellView> getSafeZoneView() {
		return safeZoneView;
	}
}
