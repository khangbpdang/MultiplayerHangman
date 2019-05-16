
/**
 * Move - a class to represent a player or the server's move/character choice
 */
import java.io.*;
public class Move implements Serializable
{
    private String name; //
    private Character response;
    
    // Default constructor
    public Move(){
        name = new String(""); // Name of player
        response = new Character('/'); // Placeholder character
    }
    
    // Create a Move object and set player's name and character response
    public Move (String name, char response) {
        this.name = name;
        this.response = response;
    }
    
    // Get name
    public String getName() {
        return name;
    }
    
    // Get character choice
    public Character getResponse() {
        return response;
    }
    

}
