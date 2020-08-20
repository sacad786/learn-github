import java. util. Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class EasyProblems {
    public int[] getMultipliedArr(int[] getMultipliedArray){
        int length = getMultipliedArray.length;
        int output[] = new int[length];
        for (int i = 0; i < length; i++) {
            output[i] = getMultipliedArray[i] * 2;
        }
        return output;
    }
//    We'll pass you an array of two numbers. Return the sum of those two numbers plus the sum of all the numbers between them.
//    The lowest number will not always come first.
//    For example, sumAll([4,1]) should return 10 because sum of all the numbers between 1 and 4 (both inclusive) is 10.

//    public static void main(String[] args) {
//        int [] array = {4,1};
//        Arrays. sort(array);
//
//        int start = array[0];
//        int end = array[array.length-1];
//        int num = 0;
//        int sum = 0;
////        int[] arr = new int[]{};
//        for (int i = start; i <= end; i++) {
//            int num1 = num + start;
//            System.out.println(num1);
//            num++;
//        }
//        for (int i = 0; i < arr.length; i++){
//            sum += arr[i];
//        }
//        return sum;
//    }

//    Compare two arrays and return a new array with any items only found in one of the two given arrays, but not both.
//    In other words, return the symmetric difference of the two arrays.
//
//    Note
//    You can return the array with its elements in any order.

    public static String[] diffBetweenArrays(String[] firstArray, String[] secondArray ) {
        String[] both = Stream.concat(Arrays.stream(firstArray), Arrays.stream(secondArray))
                .toArray(String[]::new);
//        String[] both = ArrayUtils.addAll(first, second);
        HashMap<String, Integer> newObj = new HashMap<String, Integer>();
        int minFrequency = 0;
        String minElement = "";
        for (int i = 0; i < both.length; i++){
            int newFrequency = newObj.get(both[i]);
            newObj.compute(both[i], (key, value) -> value == null ? 1 : value + 1);
            if (newFrequency < minFrequency){
                minFrequency = newFrequency;
                minElement = both[i];
            }
        }

        return both;
    }
}


