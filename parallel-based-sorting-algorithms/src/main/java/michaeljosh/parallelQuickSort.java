package michaeljosh;

import java.util.concurrent.*;

public class parallelQuickSort {

    private static final int CUTOFF = 1000;

    public static void quickSortParallel(int[] array, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        pool.invoke(new QuickSortTask(array, 0, array.length - 1));
    }

    private static class QuickSortTask extends RecursiveAction {
        private final int[] array;
        private final int left;
        private final int right;

        QuickSortTask(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            int i = left, j = right;
            int tmp;
            int pivot = array[(left + right) / 2];

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

    public static void main(String[] args) {
        int[] array = { 12, 4, 5, 6, 7, 3, 1, 15, 8, 10, 14, 2, 13, 9, 11 };

        System.out.println("Original array:");
        printArray(array);

        int numThreads = Runtime.getRuntime().availableProcessors();
        quickSortParallel(array, numThreads);

        System.out.println("\nSorted array:");
        printArray(array);
    }

    private static void printArray(int[] array) {
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
