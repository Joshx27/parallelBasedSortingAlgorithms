package michaeljosh;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class parallelRadixSortTest {

    @Test
    public void testRadixSort() {
        // Create an instance of the parallelRadixSort class
        parallelRadixSort sorter = new parallelRadixSort();

        // Create an array for testing
        int[] arrayToSort = { 4, 2, 7, 1, 9, 5, 3, 8, 6 };

        // Call the radixMulti method to sort the array
        int[] sortedArray = sorter.radixMulti(arrayToSort);

        // Verify that the array is sorted
        for (int i = 0; i < sortedArray.length - 1; i++) {
            assertTrue(sortedArray[i] <= sortedArray[i + 1]);
        }
    }
}
