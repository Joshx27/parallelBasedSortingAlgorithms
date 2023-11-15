package michaeljosh;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

import static michaeljosh.createBigArray.createArray;
import static michaeljosh.createBigArray.randomArray;
import static org.junit.Assert.assertArrayEquals;

public class bigArraySortTest {

    @Test
    public void bitonicSortTest() {
        int sortedArray[] = createArray(1000000);
        int a[] = createArray(1000000);
        randomArray(a);
        bitonicSort bitonic = new bitonicSort();
        bitonic.sort(a);
        assertArrayEquals(sortedArray, a);
    }

    @Test
    public void parallelBitonicSortTest() {
        int sortedArray[] = createArray(1000000);
        int a[] = createArray(1000000);
        randomArray(a);
        parallelBitonicSort ob = new parallelBitonicSort(a, 0, a.length, true);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(ob);
        assertArrayEquals(sortedArray, a);
    }
}
