
import java.util.*;
import java.io.*;
import java.net.*;
public class MultiplayerHangmanThread extends Thread
{
    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Leaderboard lboard;
    public MultiplayerHangmanThread(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, Leaderboard lboard) {
        super("MultiplayerHangmanThread");
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.lboard = lboard;
    }

    public void run() {

        try {
            synchronized(this){
                BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));

                // Variables to determine server's random response
                //String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String abc = "algeria";
                Random rand = new Random();

                // Variables to determine client's incoming message and 
                // server's outgoing message
                Message inMessage, outMessage;

                // Variable to represent user's character guess for the game
                Character input = new Character(' ');

                // Variable to record game outputs  
                String update = new String();

                // Variable to record terminal outputs and message to user from server
                String inputToClient = new String();

                // Variable to process the game and user/server's inputs
                Hangman game = new Hangman();

                // The word to be guessed
                String target = game.getTarget().toLowerCase();

                // Final message to the client before ending the game
                // and closing connection
                String finalMess = new String("");

                // Receive username from client
                String name=((Message)ois.readObject()).getResponse();

                // Initialize server and player
                Player server = new Player("Server");
                Player player = new Player(name);

                // Sending "successful connection" message to client and print out on terminal
                oos.writeObject(new Message("Server", new Move(), "Connection Established\nWelcome to the Hangman Game, "+ name +"! You have 10 lives into total!\nThere are " + target.length() + " letter(s) in this word.\nPlease begin guessing.\nThe Player will go first. Server please wait.\n--------------------------\n"));
                System.out.println("--------------------------\nConnection Established\nWelcome to the Hangman Game, " +name + "!\n--------------------------\n");            

                while ((inMessage=(Message)ois.readObject()) != null) {
                    // Game in session until there is no more lives for both server and client
                    // or the game has been won by either.
                    if (game.getTurns() > 0 && !game.isWon())  {
                        // Receive client's move and outputs to terminal
                        input = inMessage.getMove().getResponse();
                        System.out.println(inMessage.getName() + ": " + input);

                        // Game processes client's move and record outputs
                        update = game.guess(input);

                        // Update client's score based on outputs from game
                        if (update.equals("success")) {
                            player.score();                        
                            inputToClient = "\n" + Character.toUpperCase(input) + " is indeed in the word.\n" + game.getGuessList() + "\nThe current guess is " + game.getStatus() + "\nThe current scores are: " + player.getScore() + "("+name+") & " + server.getScore() + "(server)";
                        } else 
                        if (update.equals("failure") ){
                            inputToClient = "\nIncorrect guess! " + Character.toUpperCase(input) + " is not in the word.\n" + game.getGuessList() + "\nThere are only " + game.getTurns() + " live(s) left!\n";
                        } else { // Redundant case! In case something weird happens.
                            inputToClient = update + " " + player.getName()+" have just lost his turn";
                        }

                        inputToClient += "\n--------------------------\n";

                        // Check whether the game is won by client or the number
                        // of lives is 0
                        if (game.getTurns() == 0 || game.isWon())  {
                            oos.writeObject(new Message(server.getName(), new Move(), inputToClient));
                            System.out.println(inputToClient);
                            break;
                        } 

                        // Server's turn if the game has not ended
                        if (game.getTurns() > 0 && !game.isWon())  {
                            inputToClient +=  "It's the Server's turn.";
                            System.out.println(inputToClient);
                            oos.writeObject(new Message(server.getName(), new Move(), inputToClient));
                            
                        }
                        
                        // Generate server's random character input for the game
                        input =  Character.toLowerCase(abc.charAt(rand.nextInt(abc.length())));

                        // Game processes server's move and record outputs
                        update = game.guess(input);

                        // Update client's score based on outputs from game
                        if (update.equals("success")) {
                            server.score();
                            inputToClient = "Server: " + input + "\n\n" + Character.toUpperCase(input) + " is indeed in the word.\n" + game.getGuessList() + "\nThe current guess is " + game.getStatus() + "\nThe current scores are: " + player.getScore() + "(" + name + ") & " + server.getScore() + "(server)";
                        } else 
                        if (update.equals("failure") ){
                            inputToClient = "Server: " + input + "\nIncorrect guess! " + Character.toUpperCase(input) + " is not in the word.\n" + game.getGuessList() + "\nThere are only " + game.getTurns() + " live(s) left!\n";
                        } else {
                            inputToClient = "Server: " + input + "\n" + update + " " + server.getName()+" have just lost his turn";
                        }
                        
                        // Update the client on the progress of the game after server's move
                        if (game.getTurns() > 0 && !game.isWon())  {
                            inputToClient += "\n--------------------------\n";
                            System.out.println(inputToClient);
                            oos.writeObject(new Message(server.getName(), new Move(), inputToClient));
                        }
                        
                        // If server wins after server's move, end the game and display results
                        if (game.getTurns() == 0 || game.isWon())  {
                            System.out.println(inputToClient);
                            oos.writeObject(new Message(server.getName(), new Move(server.getName(), ' '), inputToClient));
                            break;
                        }
                    } else{
                        break;
                    }
                }
                
                // Determining the result of the game
                if(game.getTurns() == 0 || game.isWon()) {
                    System.out.println("The final scores are " + player.getScore() + "(" + name + ") & " + server.getScore() + " (Server)");
                    finalMess+= "The final scores are " + player.getScore() + "(" + name + ") & " + server.getScore() + " (Server)";
                    
                    // Determine the winner of the game or a draw and update leaderboard
                    // Add 1 point for a win, subtract 1 point for a loss, no point for draws
                    if (player.getScore().compareTo(server.getScore()) > 0) {
                        System.out.println(name + " wins!");
                        finalMess+= "\n" + name + " wins!";
                        lboard.record(name, 1);
                    }
                    if (player.getScore().compareTo(server.getScore()) < 0) {
                        System.out.println("Server wins!");
                        finalMess+= "\nServer wins!";
                        lboard.record(name, -1);
                    }
                    if (player.getScore().compareTo(server.getScore()) == 0) {
                        System.out.println("It's a draw and no one wins!");
                        finalMess+= "\nIt's a draw and no one wins!";
                        lboard.record(name, 0);
                    }
                } else { //Redundant case! In case something weird happens.
                    System.out.println("No one wins! The current scores are: " + player.getScore() + " (" + name + ") & " + server.getScore() + "(Server)");
                    finalMess+="No one wins! The current scores are: " + player.getScore() + " (" + name + ") & " + server.getScore() + " (Server)";
                }
                
                // Game ending message to terminal and client
                System.out.println("Game Ends! Thank you, "+ name +", for playing!");
                finalMess+="\nGame Ends! Thank you, "+ name +", for playing!";
                oos.writeObject(new Message(server.getName(), new Move(server.getName(), ' '), finalMess));
                
                // Print leaderboard and send to client
                finalMess = "\n" + lboard.toString();
                System.out.println(finalMess);
                oos.writeObject(new Message(server.getName(), new Move(server.getName(), ' '), finalMess));
            }
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
}
