package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.card.Card;
import model.card.standard.Standard;
import javafx.animation.ScaleTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CardView extends StackPane{
	final private Card card;
    private final ImageView imageView;
	
	public Card getCard() {
		return card;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public CardView(Card card){
		this.card=card;
		Image image;
		if(card instanceof Standard){
			Standard card2=(Standard)card;
			String s1=card2.getSuit().toString();
			String s3=s1.substring(1);
			String s2=s1.charAt(0)+"";
			s1=card2.getName();
			s2=s2.toUpperCase();
			s3=s3.toLowerCase();
			
			image=new Image("view/"+card2.getName()+s2+s3+".png");
		}
		else{
			
			image=new Image("view/"+card.getName()+".png");
		}
		imageView =new ImageView(image);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(90);
        imageView.setFitHeight(140);
		getChildren().add(imageView);
		
		// Optional: tooltip to show the description
		Tooltip tooltip = new Tooltip(card.getDescription());  // assuming getDescription() exists
		Tooltip.install(this, tooltip);

		// Border effect
		DropShadow borderGlow = new DropShadow();
		borderGlow.setColor(Color.DEEPSKYBLUE);
		borderGlow.setWidth(20);
		borderGlow.setHeight(20);

		// Hover animation
		ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), this);
		scaleUp.setToX(1.1);
		scaleUp.setToY(1.1);

		ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), this);
		scaleDown.setToX(1.0);
		scaleDown.setToY(1.0);

		// Delay before animation triggers
		PauseTransition delay = new PauseTransition(Duration.millis(50));

		// On hover
		this.setOnMouseEntered((MouseEvent e) -> {
		    delay.setOnFinished(event -> {
		        this.setEffect(borderGlow);
		        scaleUp.playFromStart();
		    });
		    delay.playFromStart();
		});

		// On exit
		this.setOnMouseExited((MouseEvent e) -> {
		    delay.stop();
		    this.setEffect(null);
		    scaleDown.playFromStart();
		});
			
	}
	

}
