import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {




        DbFunctions db = new DbFunctions();
        Connection conn = db.connectToDb("javaGameDb", "postgres", "123");
//
        db.createUserTable(conn, "player_accounts");

        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        String user = "";
        boolean running = true;

        while (running) {

            System.out.println("Main Menu\n");
            System.out.println("Choose a number to select option.\n");
            System.out.println(String.format("User: %s", user));
            System.out.println("1 -> Start game");
            System.out.println("2 -> See all Users");

            if (user.isEmpty()) {
                System.out.println("3 -> Login / Register");
            }
            else if (!user.isEmpty()) {
                System.out.println("4 -> Logout");
                System.out.println("5 -> Options");
            }
            System.out.println("6 -> Quit");


            try {
                System.out.println("> ");
                String command = scanner.nextLine();

                switch (command) {

                    case "1":
                        // Start game. a completed game will be recorded
                        System.out.println("Start Game");

                        ArrayList<String> matchResults = game.run();
                        break;

                    case "2":

                        // our READ in CRUD,  probably showing global match history and hall of fame
                        System.out.println("Records\n");

                        db.getAllUsers(conn, "player_accounts");
                        System.out.println();
                        break;
                    case "3":

                        // no validation for now, just a raw check for the username in the db, and creating one if not found
                        System.out.println("\nLogin / Register\n");
                        System.out.println("Enter Username");
                        System.out.println("> ");
                        String username = scanner.nextLine();



                        db.createUser(conn, "player_accounts", username);

                        user = username;
                        break;


                    case "4":
                        if (!user.isEmpty()) {
                            user = "";
                        }
                        break;



                    case "5":
                        System.out.println("\nOptions:\n");
                        System.out.println("1 -> Change username");
                        command = scanner.nextLine();

                        if (command.equals("1")) {
                            System.out.println("Enter new name.");
                            command = scanner.nextLine();

                            db.updateName(conn, "player_accounts", user, command);
                            break;
                        }
                        else {
                            break;
                        }


                    case "6":
                        System.out.println("Quitting...");
                        running = false;
                        break;
                }

            }
            catch (InputMismatchException e) {
                System.out.println("Wrong input.  Numbers only, please.\n");
                scanner.next();
            }
        }
    }
}