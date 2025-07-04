package view;

import java.util.ArrayList;

import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.card.wild.Burner;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class CardsPoolView extends StackPane {
	ArrayList<CardView>cards;
   
    public CardsPoolView(int numberOfCards) {
        setPrefSize(120, 180); // Adjust as needed
        setPickOnBounds(false); // So you don’t block clicks
        double yOffsetStep=3;//make small distance
        numberOfCards/=10;
        cards=new ArrayList<>();
        
        for (int i = 0; i < numberOfCards; i++) {
        	
        	Card card=new Standard("Ace","",1,Suit.SPADE,null,null);
            CardView cardView =new CardView(card);
            cardView.showBack(); // make sure it's the back image
            cardView.setTranslateY(-i * yOffsetStep); // Slight vertical shift
            cardView.setTranslateX(i*yOffsetStep);
            getChildren().add(cardView);
            cards.add(cardView);
        }
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
    public void setCards(int numberOfCards){
    	getChildren().clear();
    	setPickOnBounds(false);
    	numberOfCards/=10;
        double yOffsetStep;
        if (numberOfCards > 5) {
            yOffsetStep = 3;
        } else if (numberOfCards > 3) {
            yOffsetStep = 4;
        } else {
            yOffsetStep = 5;
            if(numberOfCards==1)numberOfCards++;
        }
        for (int i = 0; i < numberOfCards; i++) {
        	
            CardView cardview =cards.get(i);// null or dummy card
            cardview.showBack(); // make sure it's the back image
            cardview.setTranslateY(-i * yOffsetStep);  
            cardview.setTranslateX(i * yOffsetStep);// Slight vertical shift
            getChildren().add(cardview);
        }
    	
    }
    
}