package michaeljosh;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class parallelBitonicSort extends RecursiveAction {
    private static final long serialVersionUID = 1L;
    private int[] a;
    private int lo, n;
    private boolean dir;

    private static final int THRESHOLD = 4; // Threshold for parallelization

    public parallelBitonicSort(int[] a, int lo, int n, boolean dir) {
        this.a = a;
        this.lo = lo;
        this.n = n;
        this.dir = dir;
    }

    public void compute() {
        if (n <= THRESHOLD) {
            // Sequential sorting if the size is less than or equal to the threshold
            bitonicSort(lo, n, dir);
        } else {
            int m = n / 2;

            // Parallel execution of two halves
            invokeAll(new parallelBitonicSort(a, lo, m, !dir),
                    new parallelBitonicSort(a, lo + m, n - m, dir));

            // Merge the two halves
            bitonicMerge(lo, n, dir);
        }
    }

    private void bitonicSort(int lo, int n, boolean dir) {
        // Same as your original code
        // ...
        if (n > 1) {
            int m = n / 2;
            bitonicSort(lo, m, !dir);
            bitonicSort(lo + m, n - m, dir);
            bitonicMerge(lo, n, dir);
        }
    }

    private void bitonicMerge(int lo, int n, boolean dir) {
        // Same as your original code
        // ...
        if (n > 1) {
            int m = greatestPowerOfTwoLessThan(n);
            for (int i = lo; i < lo + n - m; i++)
                compare(i, i + m, dir);
            bitonicMerge(lo, m, dir);
            bitonicMerge(lo + m, n - m, dir);
        }
    }

    private void compare(int i, int j, boolean dir) {
        if (dir == (a[i] > a[j]))
            exchange(i, j);
    }

    private void exchange(int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // n>=2 and n<=Integer.MAX_VALUE
    private int greatestPowerOfTwoLessThan(int n) {
        int k = 1;
        while (k > 0 && k < n)
            k = k << 1;
        return k >>> 1;
    }

    // Rest of the code remains the same

    public static void main(String args[]) {
        int a[] = { 3, 7, 6, 2, 1 };
        parallelBitonicSort ob = new parallelBitonicSort(a, 0, a.length, true);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(ob);
        System.out.println("\nSorted array");
        System.out.println(Arrays.toString(a));
    }
}