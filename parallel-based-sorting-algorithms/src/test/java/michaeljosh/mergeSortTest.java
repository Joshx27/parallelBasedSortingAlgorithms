package michaeljosh;

import org.junit.Test;
import java.util.Arrays;

import michaeljosh.MergeSort.*;

public class mergeSortTest {

    int arraySize = 10000000;
    long array[] = new long[arraySize];
    long startTime = System.currentTimeMillis();

    @Test
    public void testSequentialSort() {
        MergeSortUtil.arrayInit(array, 30);
        startTime = System.currentTimeMillis();
        SequentialMergeSort.mergeSort(array);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Custom sequential sorting time: " + duration);
        MergeSortUtil.isSorted(array);
    }

    @Test
    public void testSystemParallelSort() {
        MergeSortUtil.arrayInit(array, 30);
        startTime = System.currentTimeMillis();
        Arrays.parallelSort(array);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("System sorting time: " + duration);
        MergeSortUtil.isSorted(array);
    }

    @Test
    public void testCustomParallelSort() {
        MergeSortUtil.arrayInit(array, 30);
        int numberOfThreads = 4;
        startTime = System.currentTimeMillis();
        ParallelMergeSort.parallelMergeSort(array, numberOfThreads);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Custom parallel sorting time: " + duration);
        MergeSortUtil.isSorted(array);
    }
}
