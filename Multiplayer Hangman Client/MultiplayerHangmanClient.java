
/**
 * Write a description of class MultiplayerHangmanClient here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.*;
import java.net.*;
public class MultiplayerHangmanClient
{
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //if (args.length != 2) {
        //    System.err.println(
        //        "Usage: java EchoClient 127.0.0.1 4444");
        //    System.exit(1);
        //}
        //String hostName = args[0];
        //int portNumber = Integer.parseInt(args[1]);
        System.out.print('\u000C'); // Clear screen
        String hostName = "localhost";
        int portNumber = 4444; // Default port for testing

        try (
        // Socket creation to connect to server
        Socket kkSocket = new Socket(hostName, portNumber); 

        // Object stream to send object to server
        ObjectOutputStream oos = new ObjectOutputStream(kkSocket.getOutputStream()); 

        // Object stream to receive object from server
        ObjectInputStream ois = new ObjectInputStream(kkSocket.getInputStream());

        // Terminal input from user
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {

            Message inMessage;
            String inputToServer;           
            String name;
            //Message message = (Message) ois.readObject();
            //System.out.println(message.getName() + ": " + message.getResponse());

            Character input;

            System.out.println("Please enter your player tag: ");
            name = stdIn.readLine().trim();
            oos.writeObject(new Message(name, new Move(), name));

            //inMessage = (Message) ois.readObject();
            while ((inMessage=(Message) ois.readObject()) != null) {
                // Print server's incoming message
                System.out.println(inMessage.getResponse());               
                
                // When the game has ended, the server will send ' ' as a signal
                // for the game to be over.
                Character response = inMessage.getMove().getResponse();
                if (!response.equals(' ')){
                    // Record player's guess/character choice
                    input = stdIn.readLine().charAt(0);

                    // Print player's choice and send it to server
                    if (input != null) {
                        System.out.println(name + ": " + input);
                        oos.writeObject(new Message(name, new Move(name, input), ""));
                    }

                    // Get status of the game from the server and print to terminal
                    inMessage=(Message) ois.readObject(); 
                    System.out.println(inMessage.getResponse());
                }
                else {                  
                    break;
                }
            }
            // Print out result of the game
            inMessage = (Message) ois.readObject();
            System.out.println(inMessage.getResponse());
            
            // Print out leaderboard
            inMessage = (Message) ois.readObject();
            System.out.print(inMessage.getResponse());
            //oos.close();
            //ois.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
