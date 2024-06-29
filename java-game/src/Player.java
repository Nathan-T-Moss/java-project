import java.util.*;

public class Player {

    ArrayList<String> hand = new ArrayList<>();
    ArrayList<String> books = new ArrayList<>();

    ArrayList<String> knownCards = new ArrayList<>();


    public String handToString() {

        StringBuilder sb = new StringBuilder();
        for (String s : hand) {
            sb.append(s);
            sb.append(", ");
        }

        return sb.toString();
    }


    public void drawCard(String card) {
        hand.add(card);
    }

    // check if 4 of same card is in hand, and move them to books
    public void makeBooks() {

        int handSize = hand.size();
        int maxCount = 0;
        char maxFreq = 0;

        for (int i = 0; i < handSize; i++) {
            int count = 0;
            for (int j = 0; j < handSize; j++) {
                if (hand.get(i).charAt(0) == hand.get(j).charAt(0)) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                maxFreq = hand.get(i).charAt(0);
            }
        }

        // highest possible frequency is 4
        if (maxCount == 4) {

            // remove the cards from hand

            hand.remove(maxFreq + "S");
            hand.remove(maxFreq + "C");
            hand.remove(maxFreq + "H");
            hand.remove(maxFreq + "D");

            books.add(String.format("Book of %ss", maxFreq));

            // cards are out of play, so we can remove them from knownCards to prevent
            // the cpu from making illegal move by asking for unavailable cards
            knownCards.remove(Character.toString(maxFreq));
        }
    }

    public boolean hasCard(char ask) {
        for (String card : hand) {
            if (card.charAt(0) == ask) {
                return true;
            }
        }
        return false;
    }

    // return a list of all cards with same value
    public ArrayList<String> cardsOfValue(char ask) {

        ArrayList<String> returnArray = new ArrayList<>();
        //String card : hand
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).charAt(0) == ask) {
                String removedElement = hand.remove(i);
                returnArray.add(removedElement);
            }
        }


        knownCards.remove(Character.toString(ask));

        return returnArray;
    }


    // cpu methods only

    public String returnCardWithHighestFreq() {

        int handSize = hand.size();
        int maxCount = 0;
        char maxFreq = 0;

        for (int i = 0; i < handSize; i++) {
            int count = 0;
            for (int j = 0; j < handSize; j++) {
                if (hand.get(i).charAt(0) == hand.get(j).charAt(0)) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                maxFreq = hand.get(i).charAt(0);
            }
        }

        return Character.toString(maxFreq);

    }

    // the cpu will remember what you fish for
    public void addToKnownCards(String card) {
        knownCards.add(card);
    }

    // cpu decides what card to ask for
    public String chooseWhatCardToAskFor() {

        // for now it will randomly decide whether to ask for a card you asked for
        // or ask for a card that the CPU has

        Random random = new Random();
        String cardToAsk = "NULL";

        int randInt = random.nextInt(2) + 1;

        switch (randInt) {

            // CPU will ask for player for cards that the CPU has in hand
            case 1:

                // i guess an unintended benefit is that card choice will be weighted
                // to ask for cards that it has more of
                randInt = random.nextInt(hand.size());
                cardToAsk = Character.toString(hand.get(randInt).charAt(0));


            // the cpu will ask for cards that the player has fished for
            case 2:
                if (!knownCards.isEmpty()) {
                randInt = random.nextInt(knownCards.size());
                cardToAsk = Character.toString(knownCards.get(randInt).charAt(0));
                }
                else {

                }


        }
        return cardToAsk;
    }
}
