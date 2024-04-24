
//PlayerHand Class ( player OR npc hand)
//fields:
//Card[] cardArray = new Card[10] (players hand)
//int placedBet; (track what we decided how many hands we could maybe win)
//int handsWon; (track sub rounds won)

//Methods:
//void updateAllCurrCardValue() which will loop through the playerHand and update all currCardValues via setCurrCardValue();
//void setBetValue() will set the value placedBet based on their total card values (AND MAYBE the bets already placed/remaining.)
//void incrementHandsWon() will add 1 to handsWon
//boolean validPlay(Card playedCard) Called during playCard, if the played card was non-valid(player didn't follow suit and could have), we return false, else true;
//void playCard(int index) which removes and plays the player selected card (for non-npc only) via looping validPlay(cardHere) to ensure good choice, and Table.addCard(cardHere) when done


import java.util.InputMismatchException;
import java.util.Scanner;

public class PlayerHand {


    //fields
    final Table table;
    private int playerNum;
    private  Card[] cardArray;
    private int handsWon;
    private int placedBet;
    private String playerName;
    private  boolean isEmpty;
    private int score;
    //constructor

    public PlayerHand(int playerNum,Table table) {
        this.playerNum = playerNum;
        this.table = table;
        this.cardArray = new Card[0];
        this.handsWon = 0;
        this.placedBet = 0;
        this.isEmpty = true;
        this.score = 0;
        this.playerName = setPlayerName();
    }
    private String setPlayerName(){
        switch (this.playerNum){
            case 0: return askForPlayerName();
            case 1: return "Bob";
            case 2: return "Sally";
            case 3: return "Tom";
            default: return "John";
        }
    }

    private String askForPlayerName() {

        Scanner sc = new Scanner(System.in);
        String userInput;
        System.out.println("Welcome to Oh Hell!\nLets start with you entering your name please:");
        while (true) {
            try {
                userInput=sc.nextLine();
            } catch (InputMismatchException e) {
                sc = new Scanner(System.in);
                continue;
            }

            return userInput;
        }
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void setCardArray(Card[] cardArray) {
        this.cardArray = cardArray;
    }

    public void addScore(int score) {
        this.score += score;
    }


    public int getHandsWon() {
        return handsWon;
    }
    public void setHandsWon(int handsWon){
        this.handsWon = handsWon;
    }

    public int getPlacedBet() {
        return placedBet;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
    public void setIsEmpty(boolean isEmpty){
        this.isEmpty = isEmpty;
    }

    public int getPlayerNum() {
        return playerNum;
    }
    public int getScore() {
        return score;
    }


    //other methods


    //we must add the card to a new slot, thus, we must increase our array by 1
    public void addCardToHand(Card card){

        Card[] newCardArray = new Card[cardArray.length+1];
        for(int i = 0; i < cardArray.length; i++){
            newCardArray[i] = cardArray[i];
        }
        card.setPlayedBy(playerNum);
        newCardArray[newCardArray.length-1] = card;
        this.cardArray = newCardArray;
        setIsEmpty(false);
    }


//void playCard(int index) which removes and plays the player selected card (for non-npc only) via looping validPlay(cardHere) to ensure good choice, and Table.addCard(cardHere) when done

//void playBestCard(); Which goes through and compares the cards, and  the highest value card based on currCardValue is played (for npc only)
//(for above, if npc bet is matching the hands Won, we want to "throw". if we want to win, but can't beat "topdog", we also want to throw. If we can win, and want to, we go for a high value card that is above topdog)
//to throw, we pick a low value card
    public void playBestCard(){
        int chosenValue = 1;
        int chosenIndex = 0;
        //we will find the index of the card we wish to play, and send it to the other play method.
        updateAllCurrCardValues();
        //we must check if we want this card to win. (either we have a match and throw, or we want to take the tricks)
        //if we don't want to win this hand and are the initiator:
        if(handsWon == placedBet && table.getTopDog() == null) {
            initiateToLose();
        }

        //in the case that we do want to win and there is no top dog, we throw the highest non-trump
        else if(handsWon != placedBet && table.getTopDog() == null){
            initiateToWin();
        }
        //if we don't want to win this hand, we will attempt to throw the highest non-winning card we can,
        else if(handsWon == placedBet) {
            throwHighestNonWinningCard();
        } //end throwing case

        //if we want to win
        else{
            playHighestAboveTopDog();
        }
         }
    private void throwLowestCard(){
        int chosenValue = 99;
        int chosenIndex = -1;
        for(int i = 0; i  < cardArray.length;i++){
            //We want to find the card that is the lowest to throw
            if(chosenValue > cardArray[i].getCurrCardValue() && validPlay(cardArray[i])){
                chosenIndex = i;
                chosenValue = cardArray[i].getCurrCardValue();
            }
        }
        //play highest non-trump
        playCard((chosenIndex));
    }
    private void throwHighestNonWinningCard(){
        int chosenValue = -1;
        int chosenIndex = -1;
        boolean validMatchFound = false;
        for(int i = 0; i < cardArray.length; i++) {
            //we will check for the highest card below top dog, and save that info, (if the card is a valid play)

            if(cardArray[i].getCurrCardValue() < table.getTopDog().getCurrCardValue() && cardArray[i].getCurrCardValue() > chosenValue &&validPlay(cardArray[i])){
                chosenIndex = i;
                validMatchFound = true;
                chosenValue = cardArray[i].getCurrCardValue();
            }
        }
        //play highest loser
        if(validMatchFound) playCard(chosenIndex);
        //if we did not find a card that can win us the hand, then we must throw.
        else throwLowestCard();

    }
    private void playHighestAboveTopDog(){
        int chosenValue = 999;
        int chosenIndex = -1;
        //We want to find the card that is the lowest in our hand that is still above top dog.
        boolean validMatchFound = false;
        for(int i = 0; i < cardArray.length; i++) {
            if (chosenValue > cardArray[i].getCurrCardValue() && cardArray[i].getCurrCardValue() > table.getTopDog().getCurrCardValue() && validPlay(cardArray[i])) {
                validMatchFound = true;
                chosenIndex = i;
                chosenValue = cardArray[i].getCurrCardValue();
            }
        }
        if(validMatchFound) playCard(chosenIndex);
        //if we did not find a card that can win us the hand, then we must throw lowest card we can
        else throwLowestCard();
    }
    private void initiateToWin(){
        int chosenValue =1;
        int chosenIndex = -1;
        //play highest non-trump
        if(!hasNonTrump()) {
            for (int i = 0; i < cardArray.length; i++) {
                //We want to find the card that is the highest b
                if (chosenValue < cardArray[i].getCurrCardValue() && validPlay(cardArray[i])) {
                    chosenIndex = i;
                    chosenValue = cardArray[i].getCurrCardValue();
                }
            }
        }
        else {
            for (int i = 0; i < cardArray.length; i++) {
                //We want to find the card that is the highest but below trump
                if (chosenValue < cardArray[i].getCurrCardValue() && cardArray[i].getCurrCardValue() < 15 && validPlay(cardArray[i])) {
                    chosenIndex = i;
                    chosenValue = cardArray[i].getCurrCardValue();
                }
            }
        }
        if(chosenIndex==-1)playHighestAboveTopDog();
        else playCard((chosenIndex));


    }

    private boolean hasNonTrump() {
        for(int i = 0; i < this.cardArray.length;i++){
            if(cardArray[i].getCurrCardValue() <15){
                return true;
            }

        }
        return false;
    }

    private void initiateToLose(){
        int chosenValue = 999;
        int chosenIndex = -1;
        for(int i = 0; i < cardArray.length; i++) {
            //we will check for the lowest card we have and if the card is valid to play
            if (cardArray[i].getCurrCardValue() < chosenValue && validPlay(cardArray[i])) {
                chosenIndex = i;
                chosenValue = cardArray[i].getCurrCardValue();
            }

        }
        //play Lowest card
        playCard(chosenIndex);
        //no need to worry about and else case, as we are initiator we will always be able to find a valid card
    }
    //this will get valid index from player for a card to play
    //this method takes input, checks it is valid and in range, and sends it back
    private int getIndex(int min, int max) {
        //we loop until valid input
        Scanner sc = new Scanner(System.in);
        int input;
        while (true) {
            System.out.println("Please choose a card from your hand, between " + min + " and " + max);
            try {
                input = sc.nextInt();
            } catch (InputMismatchException e) {
                //reset buffer from invalid input
                sc = new Scanner(System.in);
                continue;
            }
            if (input <= max || input >= min) {
                //if we have a valid number, we must insure it is a playable card.
                if (validPlay(cardArray[input - 1]))
                    return input - 1;
                System.out.println("\nYou have selected an out of suit card!\n");
            }
        }

    }
    public void getPlayCardIndex(){
        if(cardArray.length == 1)
            System.out.println( table.toString() + "\nAnd your hand is: " + this.toString() + "\nPlease pick your only card, option 1");

        else{
            System.out.println( table.toString() + "\nAnd your hand is: " + this.toString() + "\nPlease pick a card from 1-" + cardArray.length);
        }
            playCard(getIndex(1,cardArray.length));
    }
    //Removes card from players hand, placing on table
    //!!assumed validPlay has been called!!
    private void playCard(int index){

        Card temp = cardArray[index];
        cardArray[index] = null;
        Card[] newCardArray = new Card[cardArray.length-1];
        int j = 0;
        for(int i = 0; i < cardArray.length; i++){
            if(cardArray[i] != null){
                newCardArray[j] = cardArray[i];
                j++;
            }
        }
        cardArray = newCardArray;
        table.addCard(temp);
        System.out.println(this.playerName+ " played " +temp.toString() );
        if(cardArray.length == 0)
            setIsEmpty(true);
    }
    private boolean validPlay(Card playedCard){
        //if the played card matches lead suit
        if(playedCard.getCardSuit().equals(table.getLeadSuit())||table.getLeadSuit().equals(""))
            return true;
        //if the played suit doesn't match lead, we will check to see if another card COULD have been played. if so, then the choice is invalid.
        boolean hasSuit;
        for(Card card : this.cardArray){
            if(card.getCardSuit().equals(table.getLeadSuit()))
                return false;
        }
        //if we have no other cards that are lead suit, then we allow this non-following suit card
        return true;
    }
    public void setAIBetValue(){
    int bidCounter = 0;
    updateAllCurrCardValues();
    for(Card card : cardArray){
        //if we have an ace, a trump above 6, or a trump at all with 3 or less cards, we bid on that card
        if(card.getCurrCardValue() ==14 || card.getCurrCardValue() >26 || (card.getCurrCardValue() >20 && cardArray.length < 3))
            bidCounter++;
        }
    this.placedBet = bidCounter;
    System.out.println(playerName+" Bet "+placedBet);
    }
    public void setPlayerBetValue(){
        Scanner sc = new Scanner(System.in);
        int in;
        //we format based on board
        Table.delay();
        System.out.println("\n----Your turn to bet!-----\n");
        Table.delay();
        if(table.getCardsDown() == 0)
        System.out.println("Trump: |"+ table.getTrump().toString()+"|\nYour Hand: "+ this.toString()+"\n");
        else{
            System.out.println(table.toString()+"\nYour Hand:"+ this.toString()+"\n");
        }
        while(true) {
            System.out.println("Please place a bet on how many hands you think you can win");
            try {
                in = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You entered an invalid bet value. Please enter a value from 0-" + this.cardArray.length);
                //reset buffer
                sc = new Scanner(System.in);
                continue;
            }
            if(in <= cardArray.length && in >= 0) {
                this.placedBet = in;
                System.out.println("You've bet " + placedBet + "\n");
                Table.delay();
                break;
            }
        }
    }
    public void updateAllCurrCardValues(){
        for(Card card : cardArray){
            card.setCurrCardValue();
        }
    }
    public void incrementHandsWon() {

        this.handsWon++;
    }
    @Override
    public String toString(){
        String s = "";
        for(int i = 0;i < cardArray.length;i++){
            s+= "|"+cardArray[i].toString()+"| ";
        }
        return s;
    }
}
