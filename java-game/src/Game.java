import java.util.*;

public class Game {

    ArrayList<String> cards;

    public void createDeck() {
        cards = new ArrayList<String>(
                List.of(
                        "KS", "QS", "JS", "0S", "9S", "8S", "7S", "6S", "5S", "4S", "3S", "2S", "AS",
                        "KC", "QC", "JC", "0C", "9C", "8C", "7C", "6C", "5C", "4C", "3C", "2C", "AC",
                        "KH", "QH", "JH", "0H", "9H", "8H", "7H", "6H", "5H", "4H", "3H", "2H", "AH",
                        "KD", "QD", "JD", "0D", "9D", "8D", "7D", "6D", "5D", "4D", "3D", "2D", "AD"
                )
        );
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public String removeAndReturnTopCard() {

        if (!cards.isEmpty()) {
            return cards.removeFirst();
        }
        else {
            return "NONE";
        }
    }

    public void enterToContinue() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        System.out.println("Press /ENTER/ to continue.");

        while (running) {
            if ("".equals(command)) {
                running = false;
            }
            command = scanner.nextLine();
        }

    }


    // method to play the game will return values to save to db
    // such as who won and turns taken
    public ArrayList<String> run() {

        // bunch of variables to declare
        boolean running = true;
        boolean playerInputLoop = true;
        int gameLength = 0;
        Scanner scanner = new Scanner(System.in);
        Player player = new Player();
        Player cpu = new Player();
        Game deck = new Game();

        // deck setup and shuffle
        deck.createDeck();
        deck.shuffleDeck();

        // every player gets dealt 7 cards
        for (int i = 0; i < 7; i++) {
            player.drawCard(deck.removeAndReturnTopCard());
            cpu.drawCard(deck.removeAndReturnTopCard());
        }

        // We'll let the player always go first, because that's how we roll
        System.out.println("Hello Welcome to Go-Fish!");
        System.out.println("Guess which cards your opponent has!");
        System.out.println("Get more 4-of-a-kinds than your opponent to win!");
        enterToContinue();

        HashMap<String, String> cardNotationConverter = new HashMap<>();
        cardNotationConverter.put("king", "K");
        cardNotationConverter.put("queen", "Q");
        cardNotationConverter.put("jack", "J");
        cardNotationConverter.put("10", "0");
        cardNotationConverter.put("9", "9");
        cardNotationConverter.put("8", "8");
        cardNotationConverter.put("7", "7");
        cardNotationConverter.put("6", "6");
        cardNotationConverter.put("5", "5");
        cardNotationConverter.put("4", "4");
        cardNotationConverter.put("3", "3");
        cardNotationConverter.put("2", "2");
        cardNotationConverter.put("a", "A");


        while (running) {


            // building out the hud

            StringBuilder cpuBooksStringBuilder = new StringBuilder();
            for (String book : cpu.books) {
                cpuBooksStringBuilder.append(String.format("%s, ", book));
            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - -");
            System.out.println("CPU:");
            System.out.printf("Hand: %d\n", cpu.hand.size());
            System.out.printf("Books: %d (%s)\n\n", cpu.books.size(), cpuBooksStringBuilder);

            System.out.printf("Deck: %d\n\n", deck.cards.size());

            StringBuilder PlayerHand = new StringBuilder();
            for (String card : player.hand) {
                PlayerHand.append(String.format("%s, ", card));
            }
            StringBuilder PlayerBooksStringBuilder = new StringBuilder();
            for (String book : player.books) {
                PlayerBooksStringBuilder.append(String.format("%s, ", book));
            }

            System.out.println("Player:");
            System.out.printf("Hand: (%s)", PlayerHand);
            System.out.printf("Books: %d (%s)\n\n", player.books.size(), PlayerBooksStringBuilder);

            // player

            while (playerInputLoop) {
                System.out.println("Type the name or number of a card to go fish for it. [Q] to quit.");
                String command = scanner.nextLine().toLowerCase();

                if (command.equals("q")) {
                    System.out.println("Exiting game to menu.");
                    running = false;
                    break;
                }
                if (cardNotationConverter.containsKey(command)) {
                    // the player hit the right buttons, yay
                    // now we can check if the player's chosen card is in the cpu hand

                    if (cpu.hasCard(cardNotationConverter.get(command).charAt(0))) {
                        // all cards of chosen rank are removed from cpu hand and put into another arraylist chosenCards
                        ArrayList<String> chosenCards = cpu.cardsOfValue(cardNotationConverter.get(command).charAt(0));

                        // move chosen cards into your hand
                        player.hand.addAll(chosenCards);

                        // check if player can make books
                        player.makeBooks();


                        // go to cpu turn
                        break;
                    }
                    // if the selected card rank is not in the cpu's hand, then go fish
                    else {
                        System.out.println("\nCPU: GO FISH!\n");
                        player.drawCard(deck.removeAndReturnTopCard());
                        cpu.knownCards.add(cardNotationConverter.get(command));
                        // go to cpu turn
                        playerInputLoop= false;
                    }
                } else {
                    System.out.println("Not a valid card name or command");
                }
            }

            // cpu turn

            // cpu decides what card to fish for
            String fishCard = cpu.chooseWhatCardToAskFor();
            System.out.printf("CPU: Got a %s?", fishCard);

            // check if player hand has that card(s)
            if (player.hasCard(fishCard.charAt(0))) {

                ArrayList<String> chosenCards = player.cardsOfValue(fishCard.charAt(0));
                cpu.hand.addAll(chosenCards);

                cpu.makeBooks();


            } else {
                System.out.println("\nPlayer: GO FISH!\n");
                cpu.drawCard(deck.removeAndReturnTopCard());
                playerInputLoop = true;

                }


            gameLength += 1;

            if ((player.books.size() + cpu.books.size()) == 13) {
                running = false;
            }

        }

        String didWin = (player.books.size() > cpu.books.size()) ? "Win" : "Loss";
        String gameLengthString = String.valueOf(gameLength);

        ArrayList<String> matchResults = new ArrayList<>();
        matchResults.add(didWin);
        matchResults.add(gameLengthString);

        return matchResults;

    }
}
