package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
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
	final private Image faceImage;
	final private Image backImage=new Image("view/"+"back.jpg");
    private final ImageView imageView;	
    private Tooltip tooltip;

	public Card getCard() {
		return card;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public CardView(Card card){
		this.card=card;
		 ;
		if(card instanceof Standard){
			Standard card2=(Standard)card;
			String s1=card2.getSuit().toString();
			String s3=s1.substring(1);
			String s2=s1.charAt(0)+"";
			s1=card2.getName();
			s2=s2.toUpperCase();
			s3=s3.toLowerCase();
			
			faceImage=new Image("view/"+card2.getName()+s2+s3+".png");
		}
		else{
			//System.out.print(card.getName());
			faceImage=new Image("view/"+card.getName()+".png");
		}
		imageView =new ImageView(faceImage);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(120);
        imageView.setFitHeight(180);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		getChildren().add(imageView);
		
		// Optional: tooltip to show the description
		tooltip = new Tooltip(card.getDescription());  // assuming getDescription() exists
		Tooltip.install(this, tooltip);
		
		tooltip.setStyle(
			    "-fx-background-color: #8b5e3c;" +        // same rich brown
			    "-fx-text-fill: #fdf6e3;" +              // soft ivory text
			    "-fx-font-size: 14px;" +
			    "-fx-font-family: 'Georgia';" +
			    "-fx-background-radius: 12;" +           // rounded corners
			    "-fx-padding: 8;"
			);

		// Border effect
		DropShadow borderGlow = new DropShadow();
		borderGlow.setColor(Color.BROWN);
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
		    if (this.getEffect() == null) { // only apply hover effect if no selection effect
		        delay.setOnFinished(event -> {
		            this.setEffect(borderGlow);
		            scaleUp.playFromStart();
		        });
		        delay.playFromStart();
		    }
		});

		this.setOnMouseExited((MouseEvent e) -> {
		    if (this.getEffect() == borderGlow) { // only remove if it's the hover effect
		        delay.stop();
		        this.setEffect(null);
		        scaleDown.playFromStart();
		    }
		});
			
	}
	public void showBack(){
		imageView.setImage(backImage);
		Tooltip.uninstall(this, tooltip);
	}
	public void showFace(){
		imageView.setImage(faceImage);
	}
	

}
