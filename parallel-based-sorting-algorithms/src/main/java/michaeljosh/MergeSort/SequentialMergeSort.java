package michaeljosh.MergeSort;

public class SequentialMergeSort {

    public static void main(String[] args) {
        long[] array = { 12, 11, 13, 5, 6, 7 };

        System.out.println("Original array:");
        printArray(array);

        mergeSort(array);

        System.out.println("\nSorted array:");
        printArray(array);
    }

    public static void mergeSort(long[] array) {
        int length = array.length;
        if (length <= 1) {
            return; // Already sorted
        }

        int middle = length / 2;

        long[] left = new long[middle];
        long[] right = new long[length - middle];

        // Copy data to temporary arrays left[] and right[]
        System.arraycopy(array, 0, left, 0, middle);
        System.arraycopy(array, middle, right, 0, length - middle);

        // Recursively sort the two halves
        mergeSort(left);
        mergeSort(right);

        // Merge the sorted halves
        merge(array, left, right);
    }

    public static void merge(long[] array, long[] left, long[] right) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // Copy remaining elements of left[] if any
        while (i < left.length) {
            array[k++] = left[i++];
        }

        // Copy remaining elements of right[] if any
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }

    public static void printArray(long[] array) {
        for (long value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}