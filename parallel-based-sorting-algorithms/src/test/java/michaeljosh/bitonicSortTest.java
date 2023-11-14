package michaeljosh;

import org.junit.Test;


import static org.junit.Assert.assertArrayEquals;


public class bitonicSortTest {
    @Test
    public void test1() {
        int a[] = {3, 7, 4, 8, 6, 2, 1, 5};
        bitonicSort bitonic = new bitonicSort();
        bitonic.sort(a);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, a);
    }

    @Test
    public void test2() {
        int a[] = {5, 10, 15, 25, 20, 3, 2, 1};
        bitonicSort bitonic = new bitonicSort();
        bitonic.sort(a);
        assertArrayEquals(new int[]{1, 2, 3, 5, 10, 15, 20, 25}, a);
    }

}
