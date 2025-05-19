
package view;



import controller.JackarooGUI;
import javafx.geometry.Insets;
import javafx.scene.Group;
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
	private boolean isCurrent = false;
	private boolean isNext=false;
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
		isCurrent = on;
		applyHighlightEffect();
	}

	public void highlightNextPlayer(boolean on) {
		isNext = on;
		applyHighlightEffect();
	}

	private void applyHighlightEffect() {
		if (!isCurrent && !isNext) {
			this.setEffect(null);
			return;
		}

		// Combine glows if both are on
		Group effectGroup = new Group();
		if (isCurrent && isNext) {
			DropShadow blueGlow = new DropShadow();
			blueGlow.setRadius(30);
			blueGlow.setSpread(0.4);
			blueGlow.setColor(Color.DEEPSKYBLUE);

			DropShadow greenGlow = new DropShadow();
			greenGlow.setRadius(30);
			greenGlow.setSpread(0.4);
			greenGlow.setColor(Color.LIMEGREEN);
			greenGlow.setInput(blueGlow); // stack them

			this.setEffect(greenGlow);
		} else if (isCurrent) {
			DropShadow blueGlow = new DropShadow();
			blueGlow.setRadius(30);
			blueGlow.setSpread(0.4);
			blueGlow.setColor(Color.DEEPSKYBLUE);
			this.setEffect(blueGlow);
		} else if (isNext) {
			DropShadow greenGlow = new DropShadow();
			greenGlow.setRadius(30);
			greenGlow.setSpread(0.4);
			greenGlow.setColor(Color.LIMEGREEN);
			this.setEffect(greenGlow);
		}
	}
	

	
}