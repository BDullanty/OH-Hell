
//Deck class



//Fields:
//DeckStack deck (the list of total cards live in deck)
//DeckStack deadCards(the list of discarded cards , easy storage for popping back during reshuffle)

//Methods:
//void shuffleDeck(); shuffles deck stack after adding in dead cards
//Card flipTop(); pops top card in deck, sends where called (hopefully into a PlayerHand or trumpSlot)

import java.util.*;

public class Deck {


    //deck fields
    private Table table;
    private Stack<Card> deckStack ;
    //the following deck is used for toString;
    private String toString;
    //end fields

    //Deck Methods:

    public Deck(Table table) {
        this.table = table;
        this.deckStack = createNewShuffledDeckStack();

    }
    private Stack<Card> createNewShuffledDeckStack(){
        Stack<Card> deckStack = new Stack<>();
        //now we generate new cards and fill the deckStack;
        for(int i = 2; i <=53;i++){
            deckStack.push(new Card(i,table));
        }
        Collections.shuffle(deckStack);
        return deckStack;
    }

    public void shuffleDeck(){
        this.deckStack = createNewShuffledDeckStack();
    }
    //method flips top card and returns it
    public Card flipTop() {
        if (deckStack.isEmpty())
            throw new EmptyStackException();
        return deckStack.pop();

    }
    //this method returns a string that is the entire deck, top down.
    @Override
    public String toString(){
        String output = "Output is formated in type:\n\nSlotInDeck, Card\n\n";
        Object[] arrayDeck = deckStack.toArray();
        for(int i = 0; i < arrayDeck.length; i++) {
            output += (i + 1) + ": " + ((Card) arrayDeck[i]).getBasicValue() + " of " + ((Card) arrayDeck[i]).getCardSuit() + "\n";
        }
    return output;
    }

    public void dealCards() {
        //we want the players to each have Roundnumber of cards
        for(int i = 0;i<this.table.getRoundNumber();i++){
            for(int playerI = 0; playerI<5;playerI++){
                addCardToHand(playerI,flipTop());
            }
        }

    }
    private void addCardToHand(int playerI, Card card){
        PlayerHand player = (table.getPlayerList())[playerI];
        player.addCardToHand(card);
    }
}//end Deck
