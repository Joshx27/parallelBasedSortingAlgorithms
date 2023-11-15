package michaeljosh;

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
