package michaeljosh.QuickSort;

import java.util.concurrent.*;
import michaeljosh.MergeSort.MergeSortUtil;
import java.util.Arrays;

public class ParallelQuickSort {

    private static final int CUTOFF = 1000;

    public static void quickSortParallel(long[] array, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        pool.invoke(new QuickSortTask(array, 0, array.length - 1));
    }

    private static class QuickSortTask extends RecursiveAction {
        private final long[] array;
        private final int left;
        private final int right;

        QuickSortTask(long[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            int i = left, j = right;
            long tmp;
            long pivot = array[(left + right) / 2];

            // Partition
            while (i <= j) {
                while (array[i] < pivot)
                    i++;
                while (array[j] > pivot)
                    j--;
                if (i <= j) {
                    tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                    i++;
                    j--;
                }
            }

            // Recursively sort the two partitions
            if (right - left < CUTOFF) {
                if (left < j) {
                    invokeAll(new QuickSortTask(array, left, j));
                }
                if (i < right) {
                    invokeAll(new QuickSortTask(array, i, right));
                }
            } else {
                QuickSortTask leftTask = new QuickSortTask(array, left, j);
                QuickSortTask rightTask = new QuickSortTask(array, i, right);
                invokeAll(leftTask, rightTask);
            }
        }
    }

    public static void main(String args[]) {
        int numberOfThreads = 4;
        int arraySize = 2000000;
        int iterations = 10;

        long array[] = new long[arraySize];
        MergeSortUtil.arrayInit(array, 20);

        // Custom sequential merge sort
        long sequentialTotalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            long sequentialCopy[] = Arrays.copyOf(array, array.length);
            SequentialQuickSort.quickSort(sequentialCopy);
            long duration = System.currentTimeMillis() - startTime;
            sequentialTotalTime += duration;
            System.out.println("Custom sequential sorting time (Iteration " + (i + 1) + "): " + duration);
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
            System.out.println("Custom parallel QuickSort sorting time (Iteration " + (i + 1) + "): " + duration);
        }
        double customQuickSortMeanTime = (double) customQuickSortTotalTime / iterations;
        System.out.println("Mean Custom parallel QuickSort sorting time: " + customQuickSortMeanTime);

        System.out.println("Main thread has finished.");
    }
}
