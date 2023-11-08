package michaeljosh;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class parallelMergeSortTest {
    @Test
    public void testMerge() {
        long[] data = { 1, 3, 5, 2, 4, 6 };
        long[] aux = new long[data.length];

        parallelMergeSort.merge(data, aux, 0, 3, data.length);

        long[] expected = { 1, 2, 3, 4, 5, 6 };

        assertArrayEquals(expected, data);
    }
}
