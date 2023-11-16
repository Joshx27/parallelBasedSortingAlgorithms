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
    }
}
