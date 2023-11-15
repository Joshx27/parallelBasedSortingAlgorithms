package michaeljosh;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertArrayEquals;

public class parallelBitonicSortTest {

    @Test
    public void test1() {
        int a[] = {3, 7, 4, 8, 6, 2, 1, 5};
        parallelBitonicSort ob = new parallelBitonicSort(a, 0, a.length, true);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(ob);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, a);
    }

    @Test
    public void test2() {
        int a[] = {5, 10, 15, 20, 3, 2, 1};
        parallelBitonicSort ob = new parallelBitonicSort(a, 0, a.length, true);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(ob);
        assertArrayEquals(new int[]{1, 2, 3, 5, 10, 15, 20}, a);
    }

    @Test
    public void test3() {
        int a[] = {5, 10, 15, 20, 3, 2, 1, 4, 5, 8, 9, 22, 30, 12, 45, 57, 49, 50, 88, 100, 1111, 2222};
        parallelBitonicSort ob = new parallelBitonicSort(a, 0, a.length, true);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(ob);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 5, 8, 9, 10, 12, 15, 20, 22, 30, 45, 49, 50, 57, 88, 100, 1111, 2222}, a);
    }


}
