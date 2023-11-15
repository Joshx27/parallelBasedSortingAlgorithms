package michaeljosh;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class OMPRadixSort implements Callable<Void> {
    private int[] S;
    private int[] D;
    private int[][] Gl;
    private int[] LCacc;
    private int tid;
    private int nbuckets;
    private int digit_size;
    private int ndigits;
    private int N;

    public OMPRadixSort(int[] S, int[] D, int[][] Gl, int[] LCacc, int tid, int nbuckets, int digit_size, int ndigits,
            int N) {
        this.S = S;
        this.D = D;
        this.Gl = Gl;
        this.LCacc = LCacc;
        this.tid = tid;
        this.nbuckets = nbuckets;
        this.digit_size = digit_size;
        this.ndigits = ndigits;
        this.N = N;
    }

    @Override
    public Void call() {
        for (int dig = 0; dig < ndigits; dig++) {
            for (int w = 0; w < nbuckets; w++) {
                Gl[w][tid] = 0;
            }

            for (int i = 0; i < N; i++) {
                int value = getDigitValue(S[i], digit_size, dig);
                Gl[value][tid]++;
            }

            partialSum(Gl, LCacc, nbuckets, tid);
            for (int i = 0; i < N; i++) {
                int value = getDigitValue(S[i], digit_size, dig);
                D[LCacc[value]++] = S[i];
            }

            if (tid == 0) {
                swapAddresses();
            }
        }
        return null;
    }

    private int getDigitValue(int num, int digit_size, int dig) {
        int value = num >> (digit_size * dig);
        int mask = (1 << digit_size) - 1;
        return value & mask;
    }

    private void swapAddresses() {
        int[] temp = S;
        S = D;
        D = temp;
    }

    private void partialSum(int[][] Gl, int[] LCacc, int nbuckets, int tid) {
        for (int k = 0; k < nbuckets; k++) {
            int sum1 = 0;
            int sum2 = 0;

            for (int ip = 0; ip < tid; ip++) {
                sum2 += Gl[k][ip];
            }

            LCacc[k] = sum1 + sum2;
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java OMPRadixSortMain <number_of_digits> <number_of_threads> <input_filename>");
            System.exit(1);
        }

        try {
            int N; // the number of keys for sorting
            int nbuckets; // # of buckets, one for each possible value of the digit
            int ndigits; // # of digits each key will be of, user defines it
            int digit_size; // size of each digit in bits
            int[] S, LCacc, D; // Vectors: S has the keys, LCacc is the local accumulation vector,
            int[][] Gl; // Global counter matrix
            int tid; // Thread id
            int nthreads; // Number of threads
            int i, w, value;

            BufferedReader fin = new BufferedReader(new FileReader(args[2]));
            BufferedWriter fout = new BufferedWriter(new FileWriter("omp_radix.txt"));

            N = Integer.parseInt(fin.readLine()); // read the number of keys
            S = new int[N];
            D = new int[N];
            /* read the keys */
            for (i = 0; i < N; i++)
                S[i] = Integer.parseInt(fin.readLine());

            ndigits = Integer.parseInt(args[0]);
            nthreads = Integer.parseInt(args[1]);
            digit_size = (Integer.SIZE / 8) / ndigits;
            nbuckets = (int) Math.pow(2, digit_size);
            LCacc = new int[nbuckets];
            Gl = new int[nbuckets][nthreads];

            ExecutorService executor = Executors.newFixedThreadPool(nthreads);
            List<Future<Void>> futures = new ArrayList<>();

            long startTime = System.currentTimeMillis();
            for (tid = 0; tid < nthreads; tid++) {
                Callable<Void> ompRadixSort = new OMPRadixSort(S, D, Gl, LCacc, tid, nbuckets, digit_size, ndigits, N);
                futures.add(executor.submit(ompRadixSort));
            }

            // Wait for all threads to finish
            for (Future<Void> future : futures) {
                future.get();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Time of OMP Radix sort is " + (endTime - startTime) + " ms");

            // write sorted keys to omp_radix.txt
            for (int key : S)
                fout.write(key + "\n");

            executor.shutdown();
            fin.close();
            fout.close();
        } catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        }
    }
}
=======
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

/***********************************************************
 * Oblig 3 - sekvensiell kode, INF2440 v2017. Ifi, Uio, Arne Maus for store
 * verdier av n > 100 m, kjør (f.eks): >java -Xmx16000m MultiRadix 1000000000
 ************************************************************/

public class parallelRadixSort {
    int[] a;
    int n;
    boolean debug = false;
    int max = 0;
    final static int NUM_BIT = 8; // alle tall 6-11 .. finn ut hvilken verdi som
                                  // er best

    int antTraader = Runtime.getRuntime().availableProcessors();
    CyclicBarrier cb = new CyclicBarrier(antTraader + 1);

    private synchronized void setMax(int newMax) {
        max = newMax;
    }

    private void init(int[] a) {
        this.a = a;
        this.n = a.length;

        // DEL A.
        // Find largest integer.
        int start = 0;
        int end = n / antTraader;

        for (int i = 0; i < antTraader; i++) {
            Thread t = new Thread(new Runnable() {
                private int localMax = 0, localStart, localEnd;
                private int[] a;

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

                private Runnable initFinnMax(int[] a, int start, int end) {
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

    public int[] radixMulti(int[] a) {

        // 1-5 digit radixSort of : a[]
        int numBit = 2, numDigits;
        int[] bit;

        // Inital setup. Will set up class variables.
        // Includes parallelized part A.
        init(a);

        // antall binaere siffer i max
        while (max >= (1L << numBit)) {
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

        int[] t = a, b = new int[n];

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

    private void radixSort(int[] a, int[] b, int maskLen, int shift) {
        int mask = (1 << maskLen) - 1;
        int acumVal = 0;
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
                    count[(a[i] >>> shift) & mask]++;
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
                    b[count[(a[i] >>> shift) & mask]++] = a[i];
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

    void testSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                System.out.println("SorteringsFEIL på plass: " + i + " a[" + i + "]:" + a[i] + " > a[" + (i + 1) + "]:"
                        + a[i + 1]);
                return;
            }
        }
    }

    private void initArrayRandom(int len) {
        init(new int[len]);
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

    public static void writeArr(String filename, int[] x) {
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

}// SekvensiellRadix
>>>>>>> ac7df8bbd9c598c0bc22cb26a8853c9ea65194f1
