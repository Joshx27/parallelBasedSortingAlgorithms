package michaeljosh.RadixSort;

import java.util.Arrays;

public class SequentialRadixSort {

    public static void radixSort(long arr[]) {
        if (arr == null || arr.length == 0) {
            return;
        }
        long max = Arrays.stream(arr).max().getAsLong();
        for (long exp = 1; max / exp > 0; exp *= 10) {
            countingSort(arr, exp);
        }
    }

    private static void countingSort(long arr[], long exp) {
        int n = arr.length;
        long output[] = new long[n];
        int count[] = new int[10];

        Arrays.fill(count, 0);

        for (int i = 0; i < n; i++) {
            count[(int) ((arr[i] / exp) % 10)]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            output[count[(int) ((arr[i] / exp) % 10)] - 1] = arr[i];
            count[(int) ((arr[i] / exp) % 10)]--;
        }

        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }
    }
}
