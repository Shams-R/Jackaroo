package view;

import java.util.ArrayList;

import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.card.wild.Burner;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class CardsPoolView extends StackPane {
   
    public CardsPoolView(int numberOfCards) {
        setPrefSize(100, 140); // Adjust as needed
        setPickOnBounds(false); // So you don’t block clicks
        double yOffsetStep=2;//make small distance
        numberOfCards/=10;
        for (int i = 0; i < numberOfCards; i++) {
        	
        	Card card=new Standard("Ace","",1,Suit.SPADE,null,null);
            CardView cardView =new CardView(card);
            cardView.showBack(); // make sure it's the back image
            cardView.setTranslateY(-i * yOffsetStep); // Slight vertical shift
            cardView.setTranslateX(i*yOffsetStep);
            getChildren().add(cardView);
        }
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
    public void setCards(int numberOfCards){
    	getChildren().clear();
    	//this.cards=cards;
        double angleStep;//make small distance
        double yOffsetStep;
        if (numberOfCards > 5) {
            yOffsetStep = 2;
        } else if (numberOfCards > 2) {
            yOffsetStep = 3.5;
        } else {
            yOffsetStep = 5;
        }
        for (int i = 0; i < numberOfCards; i++) {
        	Card card=new Standard("Ace","",1,Suit.SPADE,null,null);
            CardView cardview =new CardView(card); // null or dummy card
            cardview.showBack(); // make sure it's the back image
            cardview.setTranslateY(-i * yOffsetStep);  
            cardview.setTranslateX(i * yOffsetStep);// Slight vertical shift
            getChildren().add(cardview);
        }
    	
    }
    
}