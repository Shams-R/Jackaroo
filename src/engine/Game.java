package engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import controller.JackarooGUI;
import engine.board.Board;
import engine.board.SafeZone;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.*;

@SuppressWarnings("unused")
public class Game implements GameManager {
    private final Board board;
    private final ArrayList<Player> players;
	private int currentPlayerIndex;
    private final ArrayList<Card> firePit;
    private int turn;

    public Game(String playerName) throws IOException {
        turn = 0;
        currentPlayerIndex = 0;
        firePit = new ArrayList<>();

        ArrayList<Colour> colourOrder = new ArrayList<>();
        
        colourOrder.addAll(Arrays.asList(Colour.values()));
        
        Collections.shuffle(colourOrder);
        
        this.board = new Board(colourOrder, this);
        
        Deck.loadCardPool(this.board, (GameManager)this);
        
        this.players = new ArrayList<>();
        this.players.add(new Player(playerName, colourOrder.get(0)));
        
        for (int i = 1; i < 4; i++) 
            this.players.add(new CPU("CPU " + i, colourOrder.get(i), this.board));
        
        for (int i = 0; i < 4; i++) 
            this.players.get(i).setHand(Deck.drawCards());
        
    }
    
    public int getTurn() {
		return turn;
	}

	public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Card> getFirePit() {
        return firePit;
    }
    
    public ArrayList<Marble> getSelectedMarbles() {
		return players.get(currentPlayerIndex).getSelectedMarbles();
	}
    
    public void selectCard(Card card) throws InvalidCardException {
    	//System.out.println(currentPlayerIndex);
        players.get(currentPlayerIndex).selectCard(card);
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {
        players.get(currentPlayerIndex).selectMarble(marble);
        
     //   System.out.println(marble);
    }

    public void deselectAll() {
        players.get(currentPlayerIndex).deselectAll();
    }

    public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException {
        if(splitDistance < 1 || splitDistance > 6)
            throw new SplitOutOfRangeException("Invalid split distance!\n" + "hint: <1,6>");

        board.setSplitDistance(splitDistance);
    }

    public boolean canPlayTurn() {
        return players.get(currentPlayerIndex).getHand().size() == (4 - turn);
    }

    public void playPlayerTurn() throws GameException {
    	
        players.get(currentPlayerIndex).play();
    }

    public void endPlayerTurn() {
        Card selected = players.get(currentPlayerIndex).getSelectedCard();
        players.get(currentPlayerIndex).getHand().remove(selected);
        if(selected!=null)
        	firePit.add(selected);
        players.get(currentPlayerIndex).deselectAll();
        
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        
        if(currentPlayerIndex == 0 && turn < 3) 
            turn++;
        
        else if (currentPlayerIndex == 0 && turn == 3) {
        	turn = 0;
        	for (Player p : players) {
              if(Deck.getPoolSize() < 4) {
	              Deck.refillPool(firePit);
	              firePit.clear();
	             // JackarooGUI.clearFirePit();

              }
              ArrayList<Card> newHand = Deck.drawCards();
              p.setHand(newHand);
        	}
        		
        }
        
    }
    public void fieldMarble(int i) throws CannotFieldException, IllegalDestroyException {
        Marble marble = players.get(i).getOneMarble();
        
        if (marble == null)
        	throw new CannotFieldException("No marbles left in the Home Zone to field.");
        
        board.sendToBase(marble,marble.getColour());
        players.get(i).getMarbles().remove(marble);
    }
     
  

    public Colour checkWin() {
        for(SafeZone safeZone : board.getSafeZones()) 
            if(safeZone.isFull()) {
                return safeZone.getColour();
            }
    
        return null;
    }

    @Override
    public void sendHome(Marble marble) {
        for (Player player : players) {
            if (player.getColour() == marble.getColour()) {
                player.regainMarble(marble);
                
     	     
    	       JackarooGUI.sound("Click.m4a");
			   JackarooGUI.sound("Marfoda.m4a");
			    
                break;
            }
        }
    }

    @Override
    public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
        Marble marble = players.get(currentPlayerIndex).getOneMarble();
        
        if (marble == null)
        	throw new CannotFieldException("No marbles left in the Home Zone to field.");
        
        board.sendToBase(marble);
        players.get(currentPlayerIndex).getMarbles().remove(marble);
    }
    
    @Override
    public void discardCard(Colour colour) throws CannotDiscardException {
        for (Player player : players) {
            if (player.getColour() == colour) {
                int handSize = player.getHand().size();
                if(handSize == 0)
                    throw new CannotDiscardException("Player has no cards to discard.");
                int randIndex = (int) (Math.random() * handSize);
                this.firePit.add(player.getHand().remove(randIndex));
                
            }
        }
    }

    @Override
    public void discardCard() throws CannotDiscardException {
        int randIndex = (int) (Math.random() * 4);
        while(randIndex == currentPlayerIndex){
            randIndex = (int) (Math.random() * 4);
            
        }

        discardCard(players.get(randIndex).getColour());
    }

    @Override
    public Colour getActivePlayerColour() {
        return players.get(currentPlayerIndex).getColour();
    }

    @Override
    public Colour getNextPlayerColour() {
        return players.get((currentPlayerIndex + 1) % 4).getColour();
    }

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

}
