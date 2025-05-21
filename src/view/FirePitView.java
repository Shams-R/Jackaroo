package view;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.card.Card;

public class FirePitView extends StackPane{
	private ArrayList<CardView>firePit;
	private static final double OFFSET = 1; // Slight offset to prevent perfect overlap
    private Random random = new Random();
    int size;

    public FirePitView() {
        // Optional styling
    	firePit=new ArrayList<>();
        setPrefSize(120, 180);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        setMouseTransparent(false);
        size=0;
        //this.setStyle("-fx-background-color: transparent;"); // Or some color to indicate firepit area
    }

    public void add(CardView card) {
    	size++;
    	firePit.add(card);
    	card.showFace();
        double angle = random.nextDouble() * 60 - 30; // Random angle between -30 and +30 degrees
        card.setRotate(angle);

        // Optional: slightly move it to avoid full overlap
        card.setLayoutX(OFFSET * getChildren().size());
        card.setLayoutY(OFFSET * getChildren().size());

        getChildren().add(card);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
    public void clear(){
    	size=0;
    	firePit.clear();
    	getChildren().clear();
    	 setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
    public void update(ArrayList<Card> cards){
    	if(cards.size()==0)
    	{
    		clear();
    		return;
    	}
    	if(firePit.size()>0&&firePit.get(firePit.size()-1).getCard()==cards.get(cards.size()-1))
    		return ;
    	
    	if(firePit.size()>=4){
    		getChildren().remove(firePit.get(0));
    		firePit.remove(0);
    	}
    	
    	
    	if(cards.size()>size+1){
    		CardView cardview1=new CardView(cards.get(cards.size()-1));
    		CardView cardview2=new CardView(cards.get(cards.size()-2));
    		add(cardview1);
    		add(cardview2);
        	return;
        	
    		
    	}
    	if(size>=2&&firePit.get(firePit.size()-2).getCard()==cards.get(cards.size()-1))
    		return ;
    	CardView cardview=new CardView(cards.get(cards.size()-1));
    	add(cardview);
    	
    }
	
	
	

}
