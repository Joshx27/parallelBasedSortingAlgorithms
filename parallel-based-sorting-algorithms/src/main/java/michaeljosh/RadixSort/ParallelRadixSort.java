package michaeljosh.RadixSort;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ParallelRadixSort {
    long[] a; // Change from int to long
    int n;
    boolean debug = false;
    long max = 0; // Change from int to long
    final static int NUM_BIT = 8;

    int antTraader = Runtime.getRuntime().availableProcessors();
    CyclicBarrier cb = new CyclicBarrier(antTraader + 1);

    private synchronized void setMax(long newMax) { // Change from int to long
        max = newMax;
    }

    private void init(long[] a) { // Change from int to long
        this.a = a;
        this.n = a.length;

        // DEL A.
        // Find largest integer.
        int start = 0;
        int end = n / antTraader;

        for (int i = 0; i < antTraader; i++) {
            Thread t = new Thread(new Runnable() {
                private long localMax = 0; // Change from int to long
                private int localStart, localEnd;
                private long[] a; // Change from int to long

                @Override
                public void run() {
                    for (int j = localStart; j < localEnd; j++) {
                        if (a[j] > localMax) {
                            localMax = a[j];
                        }
                    }
                    try {
                        setMax(localMax);
                        cb.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        System.out.println("Error " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                private Runnable initFinnMax(long[] a, int start, int end) { // Change from int to long
                    this.localStart = start;
                    this.localEnd = end;
                    this.a = a;
                    return this;
                }
            }.initFinnMax(a, start, end));

            t.start();
            start = end;
            end = (i == (antTraader - 2) ? n : end + (n / antTraader));
        }

        try {
            cb.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public long[] radixMulti(long[] a) { // Change from int to long

        // 1-5 digit radixSort of : a[]
        int numBit = 2, numDigits;
        int[] bit;

        // Inital setup. Will set up class variables.
        // Includes parallelized part A.
        init(a);

        // antall binaere siffer i max
        while (max >= (1L << numBit)) { // Change from int to long
            numBit++;
        }

        // bestem antall bit i numBits sifre
        numDigits = Math.max(1, numBit / NUM_BIT);
        bit = new int[numDigits];
        int rest = (numBit % numDigits), sum = 0;

        // fordel bitene vi skal sortere paa jevnt
        for (int i = 0; i < bit.length; i++) {
            bit[i] = numBit / numDigits;
            if (rest-- > 0)
                bit[i]++;
        }

        long[] t = a, b = new long[n]; // Change from int to long

        for (int i = 0; i < bit.length; i++) {
            radixSort(a, b, bit[i], sum); // i-te siffer fra a[] til b[]
            sum += bit[i];
            // swap arrays (pointers only)
            t = a;
            a = b;
            b = t;
        }
        if (bit.length % 2 != 0) {
            // et odde antall sifre, kopier innhold tilbake til original a[] (nå
            // b)
            System.arraycopy(a, 0, b, 0, a.length);
        }
        return a;
    }

    /**
     * Sort a[] on one digit ; number of bits = maskLen, shiftet up 'shift' bits
     */

    private void radixSort(long[] a, long[] b, int maskLen, int shift) { // Change from int to long
        int mask = (1 << maskLen) - 1;
        int numSif = mask + 1;
        int k = n / antTraader;
        CyclicBarrier synk = new CyclicBarrier(antTraader);

        if (debug) {
            System.out.printf("maskLen: %d, mask: %d, shift: %d%n",
                    maskLen, mask, shift);
            System.out.printf("n: %d, numSif: %d, antTråder: %d, k: %d%n",
                    n, numSif, antTraader, k);
        }

        int[][] allCount = new int[antTraader][];

        // Worker class for B.
        class CountWorker implements Runnable {
            int start, end;
            int[] count;
            int threadId;

            public CountWorker(int start, int end, int threadId) {
                this.threadId = threadId;
                this.start = start;
                this.end = end;
                this.count = new int[numSif];
                threadId++;
            }

            public void run() {
                // Extract relevant bits.
                for (int i = start; i < end; i++) {
                    count[(int) ((a[i] >>> shift) & mask)]++; // Change from int to long
                }
                // Put into global 2 dimentional array. Thread x digit.
                // The location specifies the digit count for the specified thread.
                allCount[threadId] = count;

                try {
                    synk.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println("Error " + e.getMessage());
                    e.printStackTrace();
                }

                // Reset count array to ready it for accumulated values.
                count = new int[numSif];

                for (int t = 0; t < numSif; t++) {
                    // Sum all threads and digits before the current digit.
                    for (int threadIndex = 0; threadIndex < antTraader; threadIndex++) {
                        for (int digit = 0; digit < t; digit++) {
                            count[t] += allCount[threadIndex][digit];
                        }
                    }
                    // Sum for all threads before current thread.
                    for (int threadIndex = 0; threadIndex < threadId; threadIndex++) {
                        count[t] += allCount[threadIndex][t];
                    }
                }

                // d) move numbers in sorted order a to b
                for (int i = start; i < end; i++) {
                    b[count[(int) ((a[i] >>> shift) & mask)]++] = a[i]; // Change from int to long
                }

                try {
                    // vent til den parallele delen er ferdig.
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println("Error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        // Start of algorithem.
        int start = 0;
        int end = k;

        for (int i = 0; i < antTraader; i++) {
            if (debug) {
                System.out.println("Starging thread with " + start + " end: " + end);
            }

            Thread t = new Thread(new CountWorker(start, end, i));
            t.start();
            start = end;
            end = (i + 2 == antTraader ? n : end + k);
        }
        try {
            // vent til den parallele delen er ferdig.
            cb.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    void testSort(long[] a) { // Change from int to long
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                System.out.println("SorteringsFEIL på plass: " + i + " a[" + i + "]:" + a[i] + " > a[" + (i + 1) + "]:"
                        + a[i + 1]);
                return;
            }
        }
    }

    private void initArrayRandom(int len) {
        init(new long[len]); // Change from int to long
        Random r = new Random(123);
        for (int i = 0; i < n; i++) {
            this.a[i] = r.nextInt(n);
        }
    }

    public void doBenchmark(int n) {
        System.out.printf("Running parallel benchmark with \t n = %d.%n", n);
        System.out.printf("\t\t Number_of_threads = %d.%n", antTraader);
        initArrayRandom(n);
        long tt = System.nanoTime();
        radixMulti(a);
        double tid = (System.nanoTime() - tt) / 1000000.0;
        System.out.printf("Sorted %d numbers with \t\t time = %f millisek.%n", n, tid);
        testSort(a);

    }

    public static void writeArr(String filename, long[] x) { // Change from int to long
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(filename));
            outputWriter.write(Arrays.toString(x));
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void write2dArr(String filename, int[][] x) {
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(filename));
            outputWriter.write(Arrays.deepToString(x).replace("], ", "]\n"));
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeNumber(String filename, double num) {
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(filename));
            outputWriter.write(new Double(num).toString());
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
    }
}
