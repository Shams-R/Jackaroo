package view;

import java.util.ArrayList;

import model.card.Card;
import javafx.scene.layout.HBox;

public class PlayerHandView extends HBox{
	private ArrayList<CardView> handCardsView;
	public PlayerHandView(ArrayList<Card> handCards) {
        setSpacing(10); // space between cards
        setStyle("-fx-padding: 10;");
        handCardsView=new ArrayList<>();

        for (Card card : handCards) {
            CardView cardView = new CardView(card);
            getChildren().add(cardView);
            this.handCardsView.add(cardView);
        }
    }
	 public void setHandCardsView(ArrayList<Card> newCards) {
	        getChildren().clear();
	        handCardsView.clear();

	        for (Card card : newCards) {
	            CardView cardView = new CardView(card);
	            handCardsView.add(cardView);
	            getChildren().add(cardView);
	        }
	    }

}
