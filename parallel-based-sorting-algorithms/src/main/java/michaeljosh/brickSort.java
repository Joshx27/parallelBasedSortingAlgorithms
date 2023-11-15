package michaeljosh;

import java.util.Arrays;

public class brickSort {
    public static void brickSort(long arr[]) {
        long n = arr.length;
        boolean isSorted = false; // Initially array is unsorted

        while (!isSorted) {
            isSorted = true;
            long temp = 0;

            // Perform Bubble sort on odd indexed element
            for (long i = 1; i <= n - 2; i = i + 2) {
                if (arr[(int)i] > arr[(int)(i + 1)]) {
                    temp = arr[(int)i];
                    arr[(int)i] = arr[(int)(i + 1)];
                    arr[(int)(i + 1)] = temp;
                    isSorted = false;
                }
            }

            // Perform Bubble sort on even indexed element
            for (long i = 0; i <= n - 2; i = i + 2) {
                if (arr[(int)i] > arr[(int)(i + 1)]) {
                    temp = arr[(int)i];
                    arr[(int)i] = arr[(int)(i + 1)];
                    arr[(int)(i + 1)] = temp;
                    isSorted = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        long arr[] = {34, 2, 10, -9};
        brickSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
