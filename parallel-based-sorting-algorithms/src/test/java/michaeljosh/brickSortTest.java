package michaeljosh;

import org.junit.Test;

import static michaeljosh.brickSort.brickSort;
import static org.junit.Assert.assertArrayEquals;

public class brickSortTest {

    @Test
    public void test1() {
        long a[] = {3, 7, 4, 8, 6, 2, 1, 5};
        brickSort(a);
        assertArrayEquals(new long[]{1, 2, 3, 4, 5, 6, 7, 8}, a);
    }

    @Test
    public void test2() {
        long a[] = {5, 10, 15, 25, 20, 3, 2, 1};
        brickSort(a);
        assertArrayEquals(new long[]{1, 2, 3, 5, 10, 15, 20, 25}, a);
    }

    @Test
    public void test3() {
        long a[] = {3, 7, 9, 8, 6, 2, 1, 5};
        brickSort(a);
        assertArrayEquals(new long[]{1, 2, 3, 5, 6, 7, 8, 9}, a);
    }

}
