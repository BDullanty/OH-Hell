
//Table (Class) (represents the area players play on)

//fields:
//int[] playedCards = new Int[5]; (sub round cards that have been played, max 5 played)
//int cardsDown; (amount of cards played)
//String trump;
//PlayerHand[] playerOrder = new PlayerHand[5]; (a list in order that we follow. Initiator starts at playerOrder[0] at the begining of each Round,(not subround)
//PlayerHand initiator; (a player that leads)
//String leadSuit; (suit lead player played at start of sub round)
//Card topDog; (current played card in the playedCards[] that is of highest value)
//int roundNumber; (keeps track of hand size when we deal everyone cards)

//void addCard(Card inCard) method that adds a card to the table list, increments size, and checks if it is top dog
//void endSubRound() this method is after each player has played one card,(we have a full table),
// we will award winner with +1 handsWon, reset the board, assign the winner as the new Initiator.(We will loop this method until players have no cards)
//void endRound() This method is after each player has no more cards. We will shuffleDeck() and start a new round with roundNumber one higher, if roundNumber doesnt indicate game end.
// we will also assign total score points to all players who matched their bet to to handswon, as well as remove points from the non-matched players.


import java.util.InputMismatchException;
import java.util.Scanner;

//we can only have one table
public class Table {
    private Deck deck;
    private PlayerHand[] playerList = new PlayerHand[5];
    private int initiator;
    private Card trump;
    //this represents the area where the player placed cards are played.
    private Card[] playedCards = new Card[5];
    //this represents the number of player cards on the table
    private int cardsDown;
    private boolean isEmpty = true;
    private String leadSuit;
    private Card topDog;
    private int roundNumber;
    private final int PLAYER_NUM= 5;


    //constructor that makes an empty board with 0 cards down and begins us on roundNumber 1
    public Table() {
        this.deck = new Deck(this);
        deck.shuffleDeck();
        this.cardsDown = 0;
        this.trump = null;
        this.initiator = 0;
        leadSuit = "";
        this.topDog = null;
        this.roundNumber = 1;
        for(int i = 0; i < PLAYER_NUM;i++) {
            playerList[i] = new PlayerHand(i,this);
        }
    }
     public Card getTrump(){
        return this.trump;
    }
    public int getCardsDown(){
        return this.cardsDown;
    }
    public PlayerHand[] getPlayerList(){
        return this.playerList;
    }
    public int getRoundNumber(){
        return this.roundNumber;
    }
    public String getTrumpSuit() {
        if(trump == null)
            return "";
        return trump.getCardSuit();
    }
    public String getLeadSuit() {
        return leadSuit;
    }

    public void setLeadSuit(String leadSuit) {
        this.leadSuit = leadSuit;
    }

    public Card getTopDog() {
        return topDog;
    }

    public void setTopDog(Card topDog) {
        this.topDog = topDog;
    }

    public void setTrump(){
        trump = this.deck.flipTop();
    }


//this method will loop our nextRound method, which will in turn run the sub round method for the size of the hands.
    public void startGame(){
        //we want 10 rounds
        //each round will run sub rounds for the length of the round, so if it is round 4, we will need 4 sub rounds.
        for(int i = 0; i < 10; i++){
            System.out.println("\n---------\nRound "+(i+1)+"\n---------\n");
            nextRound();
        }

    }
    private void nextRound(){
        //at the start of a round, we want to deal our players cards, and then flip the trump.
        deck.dealCards();
        setTrump();
        //after we change trump, we reset the currcard values
        for(PlayerHand playerHand : playerList){
            playerHand.updateAllCurrCardValues();
        }

        getBets();

        //when we run a round, we want to run "sub round" round times.
        for(int i = 0; i < roundNumber;i++){

               nextSubRound();
        }
        //will add scores and reset things for next round
        endRound();
    }


    private void nextSubRound(){
        if(this.initiator==0)System.out.println("\n\nNext sub-round!\n\nInitiator: "+playerList[initiator].getPlayerName());
        delay();
        //for each sub round, we will get a card from each player, and add it to the add card method.
        for(int i = 0; i < PLAYER_NUM; i++) {

            this.getPlayerList()[i].updateAllCurrCardValues();
            //if the player isn't ai, we let them choose
            if ((initiator + i) % PLAYER_NUM == 0) {

                System.out.println("\nYour turn to play a card!\n");
                delay();
                //the below line will take player input and play that card.
                playerList[0].getPlayCardIndex();

            }
        //if the player is AI, we play our best card option
            else{
                Table.delay();
            playerList[(initiator +i)%PLAYER_NUM].playBestCard();
                Table.delay();
        }
    }
    //after we will reset and change fields
    endSubRound();
}

    public static void delay() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e+" Issue in delay method");
        }
    }

    //void endSubRound() this method is after each player has played one card,(we have a full table),
    private void endSubRound(){
        //award winner +1 handsWon,
        PlayerHand winner = playerList[topDog.getPlayedBy()];
        winner.incrementHandsWon();
        delay();
        System.out.println("\n"+winner.getPlayerName()+ " took that hand with a "+topDog.toString()+"\n");
        delay();
        //reset the board and other vars,
        leadSuit = "";
        this.isEmpty = true;
        this.playedCards = new Card[PLAYER_NUM];
        this.initiator = topDog.getPlayedBy();
        this.topDog = null;
        cardsDown = 0;

    }
// we will award winner with +1 handsWon, reset the board, assign the winner as the new Initiator.(We will loop this method until players have no cards)
    private void endRound(){
        //we will have to check each player to see if they have a handswon to bet match, and change score each time.
        for(PlayerHand playersHand: playerList){
                playersHand.addScore(scoreChange(playersHand));
            }
        // we will announce if player went up and points they have.
        delay();
        if(playerList[0].getHandsWon() == playerList[0].getPlacedBet())
            System.out.println("You went up "+ scoreChange(playerList[0])+ "\nYour new total score is: "+playerList[0].getScore());
        else{
            System.out.println("You went down " + scoreChange(playerList[0])+ "\nYour new total score is: "+playerList[0].getScore());
        }
        delay();
        delay();
        System.out.println("Score after round "+roundNumber+":\n|"+playerList[0].getPlayerName()+": "+playerList[0].getScore()+"| |"+playerList[1].getPlayerName()+": "+playerList[1].getScore()+"| |"+playerList[2].getPlayerName()+": "+playerList[2].getScore()+"| |"+playerList[3].getPlayerName()+": "+playerList[3].getScore()+"| |"+playerList[4].getPlayerName()+": "+playerList[4].getScore()+"|\n");

        //after awarding points, we will set things
        this.deck = new Deck(this);
        deck.shuffleDeck();
        this.trump = null;
        initiator =(roundNumber)%PLAYER_NUM;
        roundNumber++;

        //we will have to reset the hands for all players
        for(int i = 0; i< 4;i++){
            //playerList[i].setCardArray(new Card[0]);
            playerList[i].setIsEmpty(true);
            playerList[i].setHandsWon(0);
        }
        //we reset the hands won back to 0

    }
    //this method will change the passed in playersHand's score based on the placed bets, and hands won.
    private int scoreChange(PlayerHand playersHand) {
        //we will calculate the difference between the numbers.

        //if the bet is greater than the hands won, we reduce by the bet
        if (playersHand.getPlacedBet() > playersHand.getHandsWon())
            return -playersHand.getPlacedBet();
        //if the bet is less than the hands won, we reduce by the hands won
        if (playersHand.getPlacedBet() < playersHand.getHandsWon())
            return -playersHand.getHandsWon();
        //if here then the bid matches wins so we increase score
        // we have special case of 0, where we return 20, else we return bet *10. We can use a ternary Statement
        return playersHand.getPlacedBet() == 0 ? 20 : playersHand.getPlacedBet()*10;
    }
    private void getBets() {
        Table.delay();
        //We will get bets after trump and cards are dealt.
        //we will go in order of initiator as first bet, and move down.
        System.out.println("Time for all players to bet, starting with Player "+playerList[initiator].getPlayerName());
        Table.delay();
        for(int i = 0; i< PLAYER_NUM; i++){
            //we have case AI and case PLAYER
            if((initiator+i)%PLAYER_NUM ==0)//player

                playerList[(initiator+i)%PLAYER_NUM].setPlayerBetValue();
            else{
                playerList[(initiator+i)%PLAYER_NUM].setAIBetValue();
            }
            Table.delay();
        }
    }

    public void addCard(Card inCard){
        //we add the card into the array
        playedCards[cardsDown] = inCard;
        //we check if it is top dog
        topDogCheck(inCard);

        //change is empty
        isEmpty = false;

        //add a card to the counter
        cardsDown++;
    }
    //we are going to check if the passed in card is better than the top dog. if so, we will make this new card top dog
    private void topDogCheck(Card inCard) {
        if(topDog == null){
            this.topDog = inCard;
            this.leadSuit = topDog.getCardSuit();
            return;
        }
        if(inCard.getCurrCardValue() > topDog.getCurrCardValue()){
            this.topDog = inCard;
           // this.leadSuit = topDog.getCardSuit();
        }
    }
    @Override
    public String toString() {
        String s ="";
        for(int i = 0; i < cardsDown;i++){
            s+= "| "+playedCards[i].toString()+"|\t";
        }
        if(cardsDown==0)
        return "Board:\nTrump: |"+trump.toString()+"|\nNo cards are played right now.\n";
        else{
            return "Board:\nTrump: |"+trump.toString()+"|\nCards Down:\n"+s+"\n";
        }
    }
}
