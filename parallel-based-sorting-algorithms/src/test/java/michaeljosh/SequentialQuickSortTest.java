package michaeljosh;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

import michaeljosh.QuickSort.SequentialQuickSort;

public class SequentialQuickSortTest {
    @Test
    public void testQuickSort() {
        long[] unsortedArray = { 4, 5, 6, 7, 3, 1, 8, 2, 9, 10 };
        long[] sortedArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        SequentialQuickSort.quickSort(unsortedArray); // Assuming you have a Mergesort class with a static
                                                      // sort
        // method

        // Assert that the sorted array matches the expected result
        assertArrayEquals(sortedArray, unsortedArray);
    }
}
