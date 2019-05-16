import java.io.*;
import java.util.*;
/**
 * Write a description of class hangman here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class Hangman implements Serializable
{
    /*private String[] words = {"australia", "austria", "azerbaijan", "china", 
            "vietnam", "cuba", "guyana", "iran", "iraq", 
           "italy", "spain", "belgium", "canada", 
            "Albania", "Algeria", "Andorra", "Angola"};*/
    
    // List of 1 word for testing purposes
    // Uncomment the list above for more choices
    private String [] words2 = {"Algeria"};
    
    // Total number of lives/turns for the game
    private int turns = 10;
    
    // List of already used characters
    private ArrayList<Character> guess_list = new ArrayList<Character>();
    
    //List of correct characters
    private ArrayList<Character> correct_list = new ArrayList<Character>();
    private String target;
    public Hangman() {
        target = words2[(new Random()).nextInt(words2.length)].toLowerCase();
    }
    
    // Check the status of word to be guessed
    // Return a String representation of the guessed word so far
    public String getStatus() {
        @SuppressWarnings("unchecked")
        char[] status = new char[target.length()];
        int counter = 0;
        for (char i: target.toCharArray()) {
            if (guess_list.contains(i)) {
                status[counter] = i;
            } else{
                if (i == ' ') {
                    status[counter] = ' ';
                }
                else {
                    if (i == '-') {
                        status[counter] = '-';
                    } else {
                        status[counter] = '_';
                    }                  
                }
            }
            counter++;
        }
        return new String(status);
    }
    
    // Processes character input sent by the player or server and product message
    public String guess(char choice) {
        // Check if character is a valid one
        // Allow only a-z or A-Z
        String pattern = new String(Character.toString(choice));
        if (!pattern.matches("[a-zA-Z]")) {
            return "Invalid inputs!";
        }
        
        // Check if character is already guessed in the guess_list
        if (guess_list.contains(Character.toLowerCase(choice))) {
            return "The character " + "\'" + Character.toUpperCase(choice) + "\'" + " is already in previous guesses.";
        }
        
        // character is valid -> add to list of guessed characters
        guess_list.add(Character.toLowerCase(choice));
        if (target.contains("" + Character.toLowerCase(choice))) {
            correct_list.add(Character.toUpperCase(choice));
            return "success";
        } else {
            turns--;
            return "failure";
        }
    }
    
    // Get the number of lives left
    public int getTurns() {
        return turns;
    }
    
    // Check if the game has been won
    public boolean isWon() {
        return !getStatus().contains("_");
    }
    
    public String getGuessList() {
            Collections.sort(guess_list);
            return "Letters used: " + getStringRepresentation(guess_list);
            //guess_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ","\t");
        
    }
    
    // Return word to be guessed
    public String getTarget() {
        return target;
    }
    
    /* @param list of characters
     * Return a string of all items in the list
     */
    private String getStringRepresentation(ArrayList<Character> list)
    {    
        StringBuilder builder = new StringBuilder(list.size());
        for(Character ch: list)
        {
            builder.append(ch);
            builder.append(" ");
        }
        return builder.toString();
    }
}
