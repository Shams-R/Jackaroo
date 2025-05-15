package view;

import java.util.ArrayList;

import model.player.Player;
import engine.board.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrackView {
	private ArrayList<CellView> mainTrack;
	
	public TrackView(ArrayList<Cell> track, ArrayList<Player> players) {
    	mainTrack = new ArrayList<>();
    	
    	int c = 0; //count for track indices
    	
    	//reference points
    	
    	int x = 159;
    	int y = 300;
    	
    	for(int i=0; i<7; i++) {
    		x+=23;
    		y-=22;
    	}
    	
    	x-=23;
    	y+=22;
    	
    	mainTrack.add(new CellView(track.get(c++),x, y));
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y+=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=32;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y-=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<7; i++) {
    		x+=23;
    		y+=22;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y+=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		y+=32;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y+=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<7; i++) {
    		x-=23;
    		y+=22;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y-=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=32;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y+=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<7; i++) {
    		x-=23;
    		y-=22;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x+=22;
    		y-=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		y-=32;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	for(int i=0; i<6; i++) {
    		x-=22;
    		y-=23;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	for(int i=0; i<6; i++) {
    		x+=23;
    		y-=22;
    		mainTrack.add(new CellView(track.get(c++),x, y));
    	}
    	
    	//Base Cells 
    	
        for(int i=0; i<4; i++) {
        	int baseIndex = (i*25+100)%100;
        	mainTrack.get(baseIndex).getCircle().setStroke(Color.valueOf(players.get(i).getColour().toString()));
        	mainTrack.get(baseIndex).getCircle().setStrokeWidth(4);
        }
    	
	}

	public ArrayList<CellView> getMainTrack() {
		return mainTrack;
	}
}
