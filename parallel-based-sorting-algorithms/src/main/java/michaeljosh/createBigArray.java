package michaeljosh;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class createBigArray {

    public static int[] createArray(int size) {
        return IntStream.range(0, size).toArray();
    }

    public static void randomArray(int array[]) {
        // Creating object for Random class
        Random rd = new Random();
        int a = array.length;

        // Starting from the last element and swapping one by one.
        for (int i = a - 1; i > 0; i--) {
            // Pick a random index from 0 to i
            int j = rd.nextInt(i + 1);

            // Swap array[i] with the element at random index
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static void main(String args[]) {
        int[] result = createArray(100);
        System.out.println("SortedArray" + Arrays.toString(result));
        randomArray(result);
        System.out.println("randomArray " + Arrays.toString(result));
    }
}
