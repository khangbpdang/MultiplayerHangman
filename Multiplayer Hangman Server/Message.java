
/**
 * Message - class to send messages between server and client
 */
import java.io.*;
public class Message implements Serializable {
    private String name; // Owner of message
    Move move; // Move of the server or client to be sent
    private String response; // actual message
    public Message(){}
    public Message (String name, Move move, String response) {
        this.name = name;
        this.move = move;
        this.response = response;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    public String getResponse() {
        return response;
    }
    public Move getMove() {
        return move;
    }
}