//Card (Class)

//Fields:


public class Card {
    //Fields
    private final Table table;
    //tracks the card value (0-52) of the card, to ID what it is
    private final int cardID;
    //tracks the suit of the card, in lower case, with s ex: "spades"
    private final String cardSuit;
    //for ai, keeps track of how "good" the card is to play right now
    private int currCardValue;
    private int playedBy;
    //the visual representation of the card, "2","King"
    private final String basicValue;
    private final int TRUMP_BASE_VALUE = 20;
    private final int CARDS_IN_A_SUIT = 13;
    private final int JACK_RAW_VALUE = 11;
    private final int QUEEN_RAW_VALUE = 12;
    private final int KING_RAW_VALUE = 0;
    private final int ACE_RAW_VALUE = 1;
    private final int KING_VALUE = 13;
    private final int ACE_VALUE = 14;
    //end Fields

    //Constructor:
    //only takes ID, and uses internal methods to produce a suit and an initial currCardValue;
    public Card(int cardID, Table table) {
        if (cardID > 53 || cardID < 1)
            throw new IllegalArgumentException("Picked a card out of range");
        this.table = table;
        this.cardID = cardID;
        this.cardSuit = setCardSuit();
        this.currCardValue = calcCurrCardValue();
        this.basicValue = setBasicValue();//we need a method to return the suit

    }//end evc
    //end Constructor
    

    //setters and getters
    public String getBasicValue() {
        return basicValue;
    }

    private String setBasicValue() {
    //we must add in a check to see if the
        return switch (this.cardID % CARDS_IN_A_SUIT) {
            case JACK_RAW_VALUE -> "Jack";
            case QUEEN_RAW_VALUE -> "Queen";
            case KING_RAW_VALUE -> "King";
            case ACE_RAW_VALUE -> "Ace";
            default -> String.valueOf(cardID % CARDS_IN_A_SUIT);
        };



    }
    public String getCardSuit() {
        return cardSuit;
    }

    public int getCurrCardValue() {
        return currCardValue;
    }

    public int getPlayedBy() {
        return playedBy;
    }


    private String setCardSuit() {
        // our CARD ID range is 2-53, each suit has 13 cards, with ranges:
        //Heart:[2-14]
        //Diamond:[15-27]
        //Spade:[28-40]
        //Club:[41-53]
        if (cardID <= 14)
            return "Hearts";
        if (cardID >= 15 && cardID <= 27)
            return "Diamonds";
        if (cardID >= 28 && cardID <= 40)
            return "Spades";
        if (cardID >= 41)
            return "Clubs";
        return "Out Of Range";

    }

    public void setCurrCardValue(){
        this.currCardValue = calcCurrCardValue();

    }
    public int calcCurrCardValue() {
        //we have two cases, one that trump is revealed and one that trump is not played.
        //case that trump is the same as the card

        //We have the card ID set when this value is called. Thus, if we take the ID and do %13, we will get the basic, non trump currCardValue, as the value should simply match card number.
        //refer to cardValues.txt and also plan.txt for more info on the below equations

        //if the card does not follow suit and is not trump, and lead has been set,
        if(! table.getTrumpSuit().equals(cardSuit) && !this.cardSuit.equals(table.getLeadSuit())&&! (table.getLeadSuit() == null||table.getLeadSuit().equals(""))){
            return 0;
        }
        //if following or initializing trump, we take normal value and add 20, if it follows lead is irrelevant
        if (table.getTrumpSuit().equals(cardSuit)||table.getTrumpSuit().equals("")) {
            //we have special cases for 0 and 1, as those are the king and ace respectively, so we will have to add 13 to those. thus, queen( value of 12, will end up 12+20)
            //King, value of 0, must end up 33
            if(cardID % CARDS_IN_A_SUIT == KING_RAW_VALUE)
                return KING_VALUE + TRUMP_BASE_VALUE;
                //Ace, value of 1, must end up 24
            if(cardID % CARDS_IN_A_SUIT == ACE_RAW_VALUE)
                return ACE_VALUE+ TRUMP_BASE_VALUE;
                //the rest are not edge case, so the card is not an ace or king, and will be set using the below formula
            return (cardID % CARDS_IN_A_SUIT) + TRUMP_BASE_VALUE;
        }

        //case that our card is not trump, and thus is either following or setting lead suit,
        else {
            //we use same logic as above, but do not add 20.
            //if edge cases
            if (cardID % CARDS_IN_A_SUIT == KING_RAW_VALUE)
                return KING_VALUE;
            if (cardID % CARDS_IN_A_SUIT == ACE_RAW_VALUE)
                return ACE_VALUE;
            return cardID % CARDS_IN_A_SUIT;


        }

    }

    public void setPlayedBy(int playedBy){
        this.playedBy = playedBy;
    }

    @Override
    public String toString() {
        return this.basicValue+" of "+this.cardSuit;}
}


