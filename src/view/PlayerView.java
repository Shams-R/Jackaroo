
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
	    imageView.setFitWidth(300);
	    imageView.setFitHeight(300);
	    imageView.setPreserveRatio(true);

	    // Create a wooden-colored rectangular border using a StackPane with padding
	    StackPane imageWithBorder = new StackPane();
	    imageWithBorder.setStyle("-fx-border-color: saddlebrown; " +
	                             "-fx-border-width: 6; " +
	                             "-fx-border-radius: 0; " +  // sharp corners
	                             "-fx-background-color: transparent;");
	    imageWithBorder.setPadding(new Insets(4)); // space between image and border
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