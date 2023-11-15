package michaeljosh.MergeSort;

/**
 * Copyright 2017 Ahmet Uyar
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
 * (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * sequential SequentialMergeSort
 * classical recursive SequentialMergeSort algorithm
 * sorting a long array
 * 
 * @author Ahmet Uyar
 */
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