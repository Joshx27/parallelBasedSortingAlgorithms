package michaeljosh;

public class quickSortTest {
        @Test
        public void testMergeSort() {
            int[] unsortedArray = { 4, 5, 6, 7, 3, 1, 8, 2, 9, 10 };
            int[] sortedArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    
            mergeSort.sort(unsortedArray); // Assuming you have a Mergesort class with a static sort method
    
            // Assert that the sorted array matches the expected result
            assertArrayEquals(sortedArray, unsortedArray);
        }
    }
    
}
