import java.util.*;
/**
 * Leaderboard - A class to represent the Overall Leaderboard of the entire 
 *               server
 */
public class Leaderboard
{
    // Contains key-value pairs identifiable by user's name input
    HashMap<String, Integer> lboard;

    public Leaderboard () {
        lboard = new HashMap<>();
    }

    // Return HashMap of leaderboard
    public HashMap<String, Integer> getLboard() {
        return lboard;
    }

    /* record scores of each player based on name string or update the score
     * parameters: a String object for name 
     *             an Integer object for score increment, should be 0, 1 or -1
     */
    public synchronized void record(String name, Integer inc) {
        if (lboard.containsKey(name)) {
            if (inc == -1 || inc == 1) {
                lboard.put(name, ((Integer) lboard.get(name)) + inc);
            } else  {
                lboard.put(name, ((Integer) lboard.get(name)));
            }
            if (((Integer) lboard.get(name)) < 0) {
                lboard.put(name, 0);
            }
        } else { // If player does not exist in leaderboard yet, insert into Hashmap
            if (inc == -1 || inc == 0) // when player loses or draw
                lboard.put(name, 0);
            else // when player wins
                lboard.put(name, 1);
        }
    }

    // Sort hashmap by values and return a HashMap object sorted by values
    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
            new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 

        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
                public int compare(Map.Entry<String, Integer> o1,  
                Map.Entry<String, Integer> o2) 
                { 
                    return (o2.getValue()).compareTo(o1.getValue()); 
                } 
            }); 

        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 

    // Return a String object that represents a sorted leaderboard
    public synchronized String toString(){
        Map<String, Integer> printedLboard = sortByValue(lboard);         
        String result = "++++++++++ LEADERBOARD! ++++++++++\n";
        for (Map.Entry<String, Integer> en : printedLboard.entrySet()) { 
            result += en.getKey() +  
            "\t" + en.getValue() + "\n"; 
        }
        result += "\n++++++++++++++++++++++++++++++++++";
        return result;
    }
}
