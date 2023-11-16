package michaeljosh.BrickSort;

import java.util.Arrays;

public class SequentialBrickSort {
    public static void brickSort(long arr[]) {
        boolean isSorted = false;
        int n = arr.length;

        while (!isSorted) {
            isSorted = true;

            // Perform Bubble sort on odd indexed elements
            for (int i = 1; i < n - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    isSorted = false;
                }
            }

            // Perform Bubble sort on even indexed elements
            for (int i = 0; i < n - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    isSorted = false;
                }
            }
        }
    }

    private static void swap(long arr[], int i, int j) {
        long temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        long arr[] = { 34, 2, 10, -9, 4, 6, 8, 9 };
        brickSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
