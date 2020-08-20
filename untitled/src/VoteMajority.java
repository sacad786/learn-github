import java.util.HashMap;

//    Create a function that returns the majority vote in an array.
//    A majority vote is an element that occurs > N/2 times
//    in an array (where N is the length of the array).
public class  VoteMajority {
    public static String majorityVotes(String[] majorityVotes) {
//        String[] majorityVotes = {"A", "B", "B", "A", "C", "C"};
        HashMap<String, Integer> newObj = new HashMap<String, Integer>();
        int maxFrequency = 0;
        String maxElement = "";
        for (int i = 0; i < majorityVotes.length; i++) {
            newObj.compute(majorityVotes[i], (key, value) -> value == null ? 1 : value + 1);
            int newFrequency = newObj.get(majorityVotes[i]);
            if (newFrequency > maxFrequency){
                maxFrequency = newFrequency;
                maxElement = majorityVotes[i];
            }
        }
        int sizeOfArray = majorityVotes.length;
        return (maxFrequency > sizeOfArray/2) ? maxElement : null;
    }

}
