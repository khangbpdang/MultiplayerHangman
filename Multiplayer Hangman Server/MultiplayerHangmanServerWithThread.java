import java.util.*;
import java.io.*;
import java.net.*;
public class MultiplayerHangmanServerWithThread
{
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.print('\u000C');
        //if (args.length != 1) {
        //    System.err.println("Usage: java KnockKnockServer 4444");
        //   System.exit(1);
        // }
        
        //int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        int portNumber = 4444; // Testing port
        
        // leaderboard for the entire server
        Leaderboard lboard = new Leaderboard();
        try (ServerSocket serverSocket = new ServerSocket(portNumber);) { 
            while (listening) {
                // Accept incoming connection from client and create a thread
                Socket socket = serverSocket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());            
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                MultiplayerHangmanThread thread = new MultiplayerHangmanThread(socket, oos, ois, lboard);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
