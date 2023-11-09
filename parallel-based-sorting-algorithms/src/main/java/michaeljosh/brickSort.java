package michaeljosh;

import java.util.Arrays;

public class brickSort {
    public static void brickSort(int arr[]) {
        int n = arr.length;
        boolean isSorted = false; // Initially array is unsorted

        while (!isSorted) {
            isSorted = true;
            int temp = 0;

            // Perform Bubble sort on odd indexed element
            for (int i = 1; i <= n - 2; i = i + 2) {
                if (arr[i] > arr[i + 1]) {
                    temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    isSorted = false;
                }
            }

            // Perform Bubble sort on even indexed element
            for (int i = 0; i <= n - 2; i = i + 2) {
                if (arr[i] > arr[i + 1]) {
                    temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    isSorted = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        int arr[] = {34, 2, 10, -9};
        brickSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
