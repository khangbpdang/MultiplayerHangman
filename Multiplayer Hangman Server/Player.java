
/**
 * Player - represent the actual human player or the server in each game instance
 */
import java.io.*;
public class Player implements Serializable {
    private String name; // Player/server name
    private Integer score; // Score within each game session, not overall leaderboard points
    
    // Constructor
    public Player(String name) {
        this.name = name;
        score = 0;
    }

    // Getters and Setters    
    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void score() {
        this.score++;
    }
}
