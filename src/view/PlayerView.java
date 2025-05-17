
package view;



import controller.JackarooGUI;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.player.Player;
public class PlayerView extends StackPane{
	private final Player player;
	private final ImageView imageView;
	private final String gender;
    public String getGender() {
		return gender;
	}
	public Player getPlayer() {
		return player;
	}
	public ImageView getImageView() {
		return imageView;
	}
	public PlayerView(Player player, String gender) {
	    this.player = player;
	    this.gender = gender;

	    // Load image based on player color
	    String colour = player.getColour().toString().toLowerCase();
	    Image image;
	    if (player.getName().contains("CPU")) {
	        image = new Image("CPU " + colour + ".png");
	    } else {
	        if (gender.equals("Female")) {
	            image = new Image("female.png");
	        } else {
	            image = new Image("male.png");
	        }
	    }

	    // Create ImageView
	    imageView = new ImageView(image);
	    imageView.setFitWidth(200);
	    imageView.setFitHeight(200);
	    imageView.setPreserveRatio(true);

	    // Clip the image to a circle
	    Circle clip = new Circle(100, 100, 100); // centerX, centerY, radius
	    imageView.setClip(clip);

	    // Wrap in a StackPane with a circular border
	    StackPane imageWithBorder = new StackPane();
	    imageWithBorder.setStyle("-fx-border-color: saddlebrown; " +
	                             "-fx-border-width: 12; " +
	                             "-fx-border-radius: 100; " +  // circular
	                             "-fx-background-radius: 100; " +
	                             "-fx-background-color: transparent;");
	    imageWithBorder.setPadding(new Insets(6));
	    imageWithBorder.setMaxSize(212, 212); // 200 image + 6*2 padding
	    imageWithBorder.setMinSize(212, 212);
	    imageWithBorder.getChildren().add(imageView);

	    // Add to this PlayerView
	    this.getChildren().add(imageWithBorder);
	    this.setPrefSize(300, 300);
	}
	
	public void highlightCurrentPlayer(boolean on) {
	    if (on) {
	        DropShadow blueGlow = new DropShadow();
	        blueGlow.setRadius(30);
	        blueGlow.setSpread(0.4);
	        blueGlow.setColor(Color.DEEPSKYBLUE); // or Color.BLUE for a stronger effect
	        this.setEffect(blueGlow);
	    } else {
	        this.setEffect(null); // remove the glow
	    }
	}

	public void highlightNextPlayer(boolean on) {
	    if (on) {
	        DropShadow greenGlow = new DropShadow();
	        greenGlow.setRadius(30);
	        greenGlow.setSpread(0.4);
	        greenGlow.setColor(Color.LIMEGREEN);
	        this.setEffect(greenGlow);
	    } else {
	        this.setEffect(null); // remove the glow
	    }
	}
	
}