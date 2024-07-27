# OH-Hell
A text-based version of the trick-taking card game "Oh Hell" written in Java. Utilizes SOLID principles to represent game components like cards, players, and the table. Features four difficult AI opponents to play against.

## Game Objective: 
End the game with the most points. 

## Gameplay flow:
- Players are give cards matching the round.
- a Trump card is shown, indicating trump suit
- Players Bid on how many "tricks" they will get that round
- Leader initiates the round by playing a card. 
- Players clockwise play their cards. If they have a card that matches the lead suit, they must play it.
- Player with the highest Value card wins the trick, and becomes the leader.
- Repeat until no more cards.
- Give points to those who had a bid that matched the tricks taken.
- Stop at 10 rounds. Winner is whoever has the largest sum of points from all the rounds.

## Game Rules:
### Following Suit
- If you have a card/cards that matches the lead suit, then you MUST play it.
- If you can't match, you can play another card.
### What Card Wins?
- Cards that are Trump beat any Lead Suit card.
- Cards that are not Trump and not Lead Suit are unable to win.
- If two cards match suit, the one that has the highest value wins.
- Within a suit, 2 is lowest, Ace is highest.
  
## Glossary
- Lead Suit : First card played in a sub round. Resets after a player takes a trick.
- Trump Suit : The suit of the Trump Card, the suit that is the most powerful this round. Changes each round.
- Trick : If you have the highest cards in a sub round, you take all the cards and call the stack a "trick"
  
