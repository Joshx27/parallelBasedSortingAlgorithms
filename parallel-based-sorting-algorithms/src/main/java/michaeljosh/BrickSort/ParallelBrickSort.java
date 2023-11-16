package michaeljosh.BrickSort;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelBrickSort {
    private static class ParallelBrickSortTask extends RecursiveAction {
        private final long[] arr;
        private final int start;
        private final int end;

        public ParallelBrickSortTask(long[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start > 1) {
                int mid = (start + end) / 2;

                invokeAll(
                        new ParallelBrickSortTask(arr, start, mid),
                        new ParallelBrickSortTask(arr, mid, end));

                // Merge the two sorted halves
                merge(arr, start, mid, end);
            }
        }

        private void merge(long[] arr, int start, int mid, int end) {
            int length1 = mid - start;
            int length2 = end - mid;

            long[] left = new long[length1];
            long[] right = new long[length2];

            System.arraycopy(arr, start, left, 0, length1);
            System.arraycopy(arr, mid, right, 0, length2);

            int i = 0, j = 0, k = start;

            while (i < length1 && j < length2) {
                if (left[i] <= right[j]) {
                    arr[k++] = left[i++];
                } else {
                    arr[k++] = right[j++];
                }
            }

            while (i < length1) {
                arr[k++] = left[i++];
            }

            while (j < length2) {
                arr[k++] = right[j++];
            }
        }
    }

    public static void parallelBrickSortArray(long arr[]) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new ParallelBrickSortTask(arr, 0, arr.length));
        forkJoinPool.shutdown();
    }

    public static void main(String[] args) {
        long arr[] = { 34, 2, 10, -9, 5, 22 };
        parallelBrickSortArray(arr);
        System.out.println(Arrays.toString(arr));
    }
}