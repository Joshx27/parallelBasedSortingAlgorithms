package michaeljosh;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import michaeljosh.BrickSort.*;
import michaeljosh.BitonicSort.*;
import michaeljosh.MergeSort.*;
import michaeljosh.QuickSort.*;
import michaeljosh.RadixSort.*;

public class PerformanceTest {
    int arraySize = 400000;
    int iterations = 10;
    int numberOfThreads = 4;
    long array[] = new long[arraySize];

    @Test
    public void javaUtilTest() {
        // for java's parallel algorithn from the concurrent library

        MergeSortUtil.arrayInit(array, 20);

        // System parallel sort
        long systemParallelTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long systemParallelCopy[] = Arrays.copyOf(array, array.length);
            Arrays.parallelSort(systemParallelCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(systemParallelCopy);
            systemParallelTotalTime += duration;
            System.out.println("System sorting time (Iteration " + (i + 1) + "): " + duration);
        }
        double systemParallelMeanTime = (double) systemParallelTotalTime / iterations;
        System.out.println("Mean System sorting time: " + systemParallelMeanTime);
    }

    @Test
    public void mergeSortTest() {

        MergeSortUtil.arrayInit(array, 20);

        // Custom sequential merge sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            SequentialMergeSort.mergeSort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(sequentialCopy);
            sequentialTotalTime += duration;
            // System.out.println("Custom sequential sorting time (Iteration " + (i + 1) +
            // "): " + duration);
        }
        double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        System.out.println("Mean Custom sequential sorting time: " + sequentialMeanTime);

        // Custom parallel merge sort
        long customParallelTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long customParallelCopy[] = Arrays.copyOf(array, array.length);
            ParallelMergeSort.parallelMergeSort(customParallelCopy, numberOfThreads);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(customParallelCopy);
            customParallelTotalTime += duration;
            // System.out.println("Custom parallel sorting time (Iteration " + (i + 1) + "):
            // " + duration);
        }
        double customParallelMeanTime = (double) customParallelTotalTime / iterations;
        System.out.println("Mean Custom parallel sorting time: " + customParallelMeanTime);

        System.out.println("Main thread has finished.");
    }

    @Test
    public void quickSortTest() {

        MergeSortUtil.arrayInit(array, 20);

        // Custom sequential merge sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            SequentialQuickSort.quickSort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            sequentialTotalTime += duration;
            // System.out.println("Custom sequential sorting time (Iteration " + (i + 1) +
            // "): " + duration);
        }
        double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        System.out.println("Mean Custom sequential sorting time: " + sequentialMeanTime);

        // Custom parallel QuickSort
        long customQuickSortTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long[] customQuickSortCopy = Arrays.copyOf(array, array.length);
            ParallelQuickSort.quickSortParallel(customQuickSortCopy, numberOfThreads);
            long duration = System.currentTimeMillis() - startTime;
            customQuickSortTotalTime += duration;
            // System.out.println("Custom parallel QuickSort sorting time (Iteration " + (i
            // + 1) + "): " + duration);
        }
        double customQuickSortMeanTime = (double) customQuickSortTotalTime / iterations;
        System.out.println("Mean Custom parallel QuickSort sorting time: " + customQuickSortMeanTime);

        System.out.println("Main thread has finished.");
    }

    @Test
    public void radixSortTest() {
        // Custom sequential merge sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            SequentialRadixSort.radixSort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            sequentialTotalTime += duration;
            // System.out.println("Custom sequential sorting time (Iteration " + (i + 1) +
            // "): " + duration);
        }
        double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        System.out.println("Mean Custom sequential sorting time: " + sequentialMeanTime);

        // Custom parallel Radix Sort
        ParallelRadixSort radixSort = new ParallelRadixSort();
        long radixSortTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long[] radixSortCopy = Arrays.copyOf(array, array.length);
            radixSort.radixMulti(radixSortCopy);
            long duration = System.currentTimeMillis() - startTime;
            radixSortTotalTime += duration;
            // System.out.println("Custom parallel Radix Sort sorting time (Iteration " + (i
            // + 1) + "): " + duration);
        }
        double radixSortMeanTime = (double) radixSortTotalTime / iterations;
        System.out.println("Mean Custom parallel Radix Sort sorting time: " + radixSortMeanTime);

        System.out.println("Main thread has finished.");
    }

    @Test
    public void bitonicSortTest() {

        MergeSortUtil.arrayInit(array, 20);

        // Custom sequential bitonic sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            SequentialBitonicSort bitonic = new SequentialBitonicSort();
            bitonic.sort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(sequentialCopy);
            sequentialTotalTime += duration;
            // System.out.println("Custom sequential sorting time (Iteration " + (i + 1) +
            // "): " + duration);
        }
        double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        System.out.println("Mean Custom sequential sorting time: " + sequentialMeanTime);

        // Custom bitonic sort
        long customParallelTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long customParallelCopy[] = Arrays.copyOf(array, array.length);
            ParallelBitonicSort ob = new ParallelBitonicSort(customParallelCopy, 0, customParallelCopy.length, true);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(ob);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(customParallelCopy);
            customParallelTotalTime += duration;
            // System.out.println("Custom parallel sorting time (Iteration " + (i + 1) + "):
            // " + duration);
        }
        double customParallelMeanTime = (double) customParallelTotalTime / iterations;
        System.out.println("Mean Custom parallel sorting time: " + customParallelMeanTime);

        System.out.println("Main thread has finished.");
    }

    @Test
    public void brickSortTest() {

        MergeSortUtil.arrayInit(array, 20);

        // // Custom sequential brick sort
        // long sequentialTotalTime = 0;
        // for (int i = 0; i < iterations; i++) {
        // long sequentialCopy[] = Arrays.copyOf(array, array.length);
        // long startTime = System.currentTimeMillis();
        // SequentialBrickSort.brickSort(sequentialCopy);
        // long duration = System.currentTimeMillis() - startTime;
        // // MergeSortUtil.isSorted(sequentialCopy);
        // sequentialTotalTime += duration;
        // System.out.println("Custom sequential sorting time (Iteration " + (i + 1) +
        // "): " + duration);
        // }
        // double sequentialMeanTime = (double) sequentialTotalTime / iterations;
        // System.out.println("Mean Custom sequential sorting time: " +
        // sequentialMeanTime);

        // Custom parallel brick sort
        long customParallelTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long customParallelCopy[] = Arrays.copyOf(array, array.length);
            long startTime = System.currentTimeMillis();
            ParallelBrickSort.parallelBrickSortArray(customParallelCopy);
            long duration = System.currentTimeMillis() - startTime;
            // MergeSortUtil.isSorted(customParallelCopy);
            customParallelTotalTime += duration;
            // System.out.println("Custom parallel sorting time (Iteration " + (i + 1) + "):
            // " + duration);
        }
        double customParallelMeanTime = (double) customParallelTotalTime / iterations;
        System.out.println("Mean Custom parallel sorting time: " + customParallelMeanTime);

        System.out.println("Main thread has finished.");

    }
}
