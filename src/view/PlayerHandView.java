package view;

import java.util.ArrayList;

import controller.JackarooGUI;
import model.card.Card;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class PlayerHandView extends HBox{
	private ArrayList<CardView> handCardsView;
	public PlayerHandView(ArrayList<Card> handCards) {
        setSpacing(10); // space between cards
        setStyle("-fx-padding: 10;");
        handCardsView=new ArrayList<>();

        for (Card card : handCards) {
            CardView cardView = new CardView(card);
            getChildren().add(cardView);
            handCardsView.add(cardView);
        }
        
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);//not to take the whole screen
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
	 
	 public ArrayList<CardView> getHandCardsView() {
		return handCardsView;
	}
	public void showBack(){
		 for(int i=0;i<handCardsView.size();i++)
			 handCardsView.get(i).showBack();
	 }
	 public int size(){
		 return handCardsView.size();
	 }
	 public CardView get(int i){
		 return handCardsView.get(i);
	 }
	 public void remove(CardView card){
		 handCardsView.remove(card);
		 getChildren().remove(card);
		 
	 }
	 
	 

}
