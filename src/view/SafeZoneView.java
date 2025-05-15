package view;

import java.util.ArrayList;

import engine.board.Cell;

public class SafeZoneView {
	private ArrayList<CellView> safeZoneView;
	
	public SafeZoneView(ArrayList<Cell> safeZoneCells ,String colour, int entryX, int entryY, int index) {
		safeZoneView = new ArrayList<CellView>();
            if (index == 0) {
            	for(int c=0; c<4; c++) {
            		entryX+=22;
            		entryY+=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            } else if (index == 1) {
            	for(int c=0; c<4; c++) {
            		entryX-=22;
            		entryY+=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            } else if (index == 2) {
            	for(int c=0; c<4; c++) {
            		entryX-=22;
            		entryY-=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            } else if (index == 3) {
            	for(int c=0; c<4; c++) {
            		entryX+=22;
            		entryY-=23;
            		safeZoneView.add(new CellView(colour, safeZoneCells.get(c), entryX, entryY));
            	}
            }
	}

	public ArrayList<CellView> getSafeZoneView() {
		return safeZoneView;
	}
}
