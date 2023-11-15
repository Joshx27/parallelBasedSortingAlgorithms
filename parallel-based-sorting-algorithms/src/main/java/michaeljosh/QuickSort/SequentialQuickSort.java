package michaeljosh.QuickSort;

public class SequentialQuickSort {

    public static void quickSort(long arr[]) {
        if (arr == null || arr.length == 0) {
            return;
        }
        int length = arr.length;
        quickSort(arr, 0, length - 1);
    }

    private static void quickSort(long arr[], int low, int high) {
        if (low < high) {
            int partitionIndex = partition(arr, low, high); // Corrected typo here
            quickSort(arr, low, partitionIndex - 1); // Corrected typo here
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    private static int partition(long arr[], int low, int high) {
        long pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                long temp = arr[i]; // Changed 'int' to 'long' here
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        long temp = arr[i + 1]; // Changed 'int' to 'long' here
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
