package view;

import controller.JackarooGUI;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.Colour;
import model.player.Marble;


public class MarbleView extends StackPane {
	private final Marble marble;
	private final ImageView imageView;
	
	public MarbleView (Marble marble){
		this.marble=marble;
		if(marble==null)
		System.out.print(marble);
		String colour=marble.getColour().toString().toLowerCase();
		Image image=new Image(colour + ".png");
		imageView =new ImageView(image);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(40);
        imageView.setFitHeight(40);
		getChildren().add(imageView);
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	}

	public Marble getMarble() {
		return marble;
	}

	public ImageView getImageView() {
		return imageView;
	}
	public Colour getColour(){
		return marble.getColour();
	}
}
