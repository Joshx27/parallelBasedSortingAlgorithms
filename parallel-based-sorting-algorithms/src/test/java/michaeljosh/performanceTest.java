package michaeljosh;

import michaeljosh.MergeSort.MergeSortUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import static michaeljosh.brickSort.brickSort;
import static michaeljosh.parallelBrickSort.parallelBrickSortArray;

public class performanceTest {

    @Test
    public void bitonicSortTest() {
        int numberOfThreads = 4;
        int arraySize = 1600000;
        int iterations = 10;

        long array[] = new long[arraySize];
        MergeSortUtil.arrayInit(array, 20);

        // Custom sequential bitonic sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            bitonicSort bitonic = new bitonicSort();
            bitonic.sort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(sequentialCopy);
            sequentialTotalTime += duration;
            System.out.println("Custom sequential sorting time (Iteration " + (i + 1) + "): " + duration);
        }
        double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        System.out.println("Mean Custom sequential sorting time: " + sequentialMeanTime);

        // Custom bitonic sort
        long customParallelTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long customParallelCopy[] = Arrays.copyOf(array, array.length);
            parallelBitonicSort ob = new parallelBitonicSort(customParallelCopy, 0, customParallelCopy.length, true);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(ob);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(customParallelCopy);
            customParallelTotalTime += duration;
            System.out.println("Custom parallel sorting time (Iteration " + (i + 1) + "): " + duration);
        }
        double customParallelMeanTime = (double) customParallelTotalTime / iterations;
        System.out.println("Mean Custom parallel sorting time: " + customParallelMeanTime);

        System.out.println("Main thread has finished.");
    }

    @Test
    public void brickSortTest() {
        int numberOfThreads = 4;
        int arraySize = 800000;
        int iterations = 1;

        long array[] = new long[arraySize];
        MergeSortUtil.arrayInit(array, 20);

        // Custom sequential brick sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            long startTime = System.currentTimeMillis();
            brickSort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(sequentialCopy);
            sequentialTotalTime += duration;
            System.out.println("Custom sequential sorting time (Iteration " + (i + 1) + "): " + duration);
        }
        double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        System.out.println("Mean Custom sequential sorting time: " + sequentialMeanTime);

        // Custom brick sort
        long customParallelTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long customParallelCopy[] = Arrays.copyOf(array, array.length);
            long startTime = System.currentTimeMillis();
            parallelBrickSortArray(customParallelCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(customParallelCopy);
            customParallelTotalTime += duration;
            System.out.println("Custom parallel sorting time (Iteration " + (i + 1) + "): " + duration);
        }
        double customParallelMeanTime = (double) customParallelTotalTime / iterations;
        System.out.println("Mean Custom parallel sorting time: " + customParallelMeanTime);

        System.out.println("Main thread has finished.");

    }
}
