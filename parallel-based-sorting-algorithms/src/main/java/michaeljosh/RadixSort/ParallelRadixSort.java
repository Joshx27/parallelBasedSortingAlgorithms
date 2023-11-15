package michaeljosh.RadixSort;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLongArray;

class ParaRadix {
    long n;
    long numberOfCores;
    long[] a;
    long[] b;
    long globalMax = 0;
    long numBit = 2;
    long[][] allCount;
    long[] sumCount;
    long[][] countRange;
    long[] partialSum;
    Thread[] t;
    CyclicBarrier cbMain;
    CyclicBarrier cb2;
    CyclicBarrier cb3;
    CyclicBarrier cb4;
    CyclicBarrier cb6;
    CyclicBarrier cb9;
    long bit1, bit2, mask, mask2;
    long startTime, endTime, elapsedTime;
    double millisecs;

    ParaRadix(final long n) {
        this.n = n;
        numberOfCores = (Runtime.getRuntime().availableProcessors());
        a = new long[(int) n];
        b = new long[(int) n];
        t = new Thread[(int) numberOfCores];
        partialSum = new long[(int) numberOfCores];
        countRange = new long[(int) numberOfCores][2];
        allCount = new long[(int) numberOfCores][];
        fillArrayWithRand(); // Fills a[] with random numbers up to n.

        cbMain = new CyclicBarrier((int) numberOfCores, new Runnable() {
            @Override
            public void run() {

                // CHANGE: Only one thread is now finding numBit
                // Finding numBit
                while (globalMax >= (1L << numBit))
                    numBit++; // antall siffer i max

                bit1 = numBit / 2;
                bit2 = numBit - bit1;

                mask = (1L << bit1) - 1;
                mask2 = (1L << bit2) - 1;
                sumCount = new long[(int) (mask + 1)];
            }
        });

        cb2 = new CyclicBarrier((int) numberOfCores, new Runnable() {
            @Override
            public void run() {
                // Calculating start and end -range for each thread in sumCount array.

                // Implemented your suggested simplified code. Thanks! (Still with exclusive
                // interval.)
                long countPerThread = (mask + 1) / numberOfCores;
                long countRest = (mask + 1) % numberOfCores;
                long startCountIndex = 0;
                long endCountIndex = countPerThread;

                for (int i = 0; i < numberOfCores; i++) {
                    if (countRest > 0) {
                        endCountIndex++;
                        countRest--;
                    }

                    countRange[i][0] = startCountIndex;
                    countRange[i][1] = endCountIndex - 1;
                    startCountIndex = endCountIndex;
                    endCountIndex += countPerThread;
                }

            }
        });

        cb3 = new CyclicBarrier((int) numberOfCores);

        cb4 = new CyclicBarrier((int) numberOfCores, new Runnable() {
            @Override
            public void run() {
                // Initializing new sumcount for step2.
                sumCount = new long[(int) (mask2 + 1)];
            }
        });

        // This is the final barrier. It checks if array is sorted.
        cb6 = new CyclicBarrier((int) numberOfCores, new Runnable() {
            @Override
            public void run() {
                endTime = System.nanoTime();
                elapsedTime = endTime - startTime;
                millisecs = (double) elapsedTime / 1000000.0;
                System.out
                        .println("\nRadix sort av " + n + " tall tok: " + millisecs + " millisek i parallell løsning.");
                System.out.println(isSorted(a));
            }
        });

        cb9 = new CyclicBarrier((int) numberOfCores, new Runnable() {
            @Override
            public void run() {
                // Calculating start and end -range for each thread in sumCount array.

                // Implemented your suggested simplified code. Thanks! (Still with exclusive
                // interval.)
                long countPerThread = (mask2 + 1) / numberOfCores;
                long countRest = (mask2 + 1) % numberOfCores;
                long startCountIndex = 0;
                long endCountIndex = countPerThread;

                for (int i = 0; i < numberOfCores; i++) {
                    if (countRest > 0) {
                        endCountIndex++;
                        countRest--;
                    }

                    countRange[i][0] = startCountIndex;
                    countRange[i][1] = endCountIndex - 1;
                    startCountIndex = endCountIndex;
                    endCountIndex += countPerThread;
                }

            }
        });

        startTime = System.nanoTime();
        findMax();
    }

    // Fills array with random numbers.
    void fillArrayWithRand() {
        // System.out.println("Generating random numbers");
        Random r = new Random(45123);
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt((int) n - 1);
        }
    }

    // Checks if an array is sorted from smallest to biggest.
    boolean isSorted(long[] arr) {
        System.out.println("Is array successfully sorted?");
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                System.out.println("Error: Array was not successfully sorted. Error triggered at index " + i + ".");
                return false;
            }
        }
        return true;
    }

    void findMax() {

        long numPerThread = n / numberOfCores;
        long numRest = n % numberOfCores;
        long startIndex = 0;
        long endIndex = numPerThread;

        // Calculates start and end -index for each thread.
        for (int i = 0; i < numberOfCores; i++) {
            if (numRest > 0) {
                endIndex++;
                numRest--;
            }
            (t[i] = new Thread(new RadixThread(startIndex, (endIndex - 1), a, cbMain, i, cb2))).start(); // Starting
                                                                                                         // thread.
            startIndex = endIndex;
            endIndex += numPerThread;
        }
    }

    synchronized void updateGlobalMaxValue(long i) {
        if (i > globalMax) {
            globalMax = i;
        }
    }

    class RadixThread implements Runnable {
        long maxValue = 0;
        long startIndex; // Start range to find max value in array.
        long endIndex; // End range to find max value in array.
        long countStartIndex;
        long countEndIndex;
        long[] count;
        long threadNumber;
        CyclicBarrier cb;
        CyclicBarrier cb2;

        RadixThread(long startIndex, long endIndex, long[] a, CyclicBarrier cb, long threadNumber, CyclicBarrier cb2) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.cb = cb;
            this.threadNumber = threadNumber;
            this.cb2 = cb2;
        }

        public void run() {
            findLocalMax();
            updateGlobalMaxValue(maxValue);

            try {
                cb.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }
            count = new long[(int) mask + 1];
            countFrequency(mask, 0, a);

            try {
                cb2.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            // Assigning indexes from countRange array.
            countStartIndex = countRange[(int) threadNumber][0];
            countEndIndex = countRange[(int) threadNumber][1];
            summize();

            try {
                cb3.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            acumValStepOne((int) (mask + 1));

            try {
                cb3.await(); // cb8
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            acumValStepTwo();

            try {
                cb3.await(); // cb8
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            moveNumbers(a, b, mask, 0);

            try {
                cb4.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            // Ferdig med første del.
            count = new long[(int) mask2 + 1];
            countFrequency(mask2, bit1, b);

            try {
                cb9.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }
            countStartIndex = countRange[(int) threadNumber][0];
            countEndIndex = countRange[(int) threadNumber][1];
            summize();

            try {
                cb3.await(); // cb5
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            acumValStepOne((int) (mask2 + 1));

            try {
                cb3.await(); // cb5
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            acumValStepTwo();

            try {
                cb3.await(); // cb 7
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

            moveNumbers(b, a, mask2, bit1);

            try {
                cb6.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }

        } // END Constructor for ParaFindMax

        void findLocalMax() {
            for (int i = (int) startIndex; i < endIndex + 1; i++) {
                if (a[i] > maxValue)
                    maxValue = a[i];
            }
        }

        void countFrequency(long mk, long shift, long[] arr) {
            for (int i = (int) startIndex; i < endIndex + 1; i++) {
                count[(int) ((arr[i] >> shift) & mk)]++;
            }
            allCount[(int) threadNumber] = count; // Henger count i den dobble arryen allCount.
        }

        void summize() {
            for (int i = 0; i < numberOfCores; i++) {
                for (int k = (int) countStartIndex; k < countEndIndex + 1; k++) {
                    sumCount[k] = sumCount[k] + allCount[(int) i][k];
                }
            }
        }

        // Adds up values in threads' partition. Saves the sum in partitialSum[].
        void acumValStepOne(int arrSize) {

            count = new long[arrSize];
            int counter = 0;
            long acumVal = 0;
            int j;

            if (threadNumber == 0) {
                for (int i = (int) countStartIndex; i < countEndIndex + 1; i++) {
                    j = (int) sumCount[i];
                    count[i] = acumVal;
                    acumVal += j;
                }
                count[(int) countEndIndex + 1] = acumVal;
                partialSum[(int) threadNumber] = acumVal;
            } else {
                acumVal = sumCount[(int) countStartIndex];
                for (int k = (int) countStartIndex; k < countEndIndex; k++) {
                    count[counter] = acumVal;
                    acumVal += sumCount[k + 1];
                    counter++;
                }
                count[counter] = acumVal;
                partialSum[(int) threadNumber] = count[counter];
            }

        }

        // Adds conditional sums from partitialSum[] to threads counts and adds to
        // global sumCount array.
        void acumValStepTwo() {
            int currVal;
            int counter = 0;
            int countStart = (int) countStartIndex;
            int countEnd = (int) countEndIndex;

            if (threadNumber != 0) {
                countStart++;
                countEnd++;
            } else {
                countEnd++;
            }

            for (int i = countStart; i <= countEnd; i++) {
                currVal = (int) count[counter];
                for (int k = 0; k < threadNumber; k++) {
                    currVal += (int) partialSum[k];
                }
                sumCount[i] = currVal;

                counter++;
                if (i + 1 == sumCount.length)
                    return;
            }
        }

        void findNumBit() {
            while (maxValue >= (1L << numBit))
                numBit++; // antall siffer i max
        }

        // Move numbers in sorted order arr1 to arr2.
        void moveNumbers(long[] arr1, long[] arr2, long mk, long shift) {
            for (int i = 0; i < arr1.length; i++) {
                if (isOneOfMine((int) ((arr1[i] >> shift) & mk))) {
                    arr2[(int) sumCount[(int) ((arr1[i] >> shift) & mk)]++] = arr1[i];
                }
            }
        }

        // Checks if given "pos" is current thread's property.
        boolean isOneOfMine(int pos) {
            if (pos >= countStartIndex && pos <= countEndIndex) {
                return true;
            }
            return false;
        }

    }

    public static void main(String[] args) {
        // Specify the size of the array
        long arraySize = 1000000;

        // Create an instance of ParaRadix
        ParaRadix paraRadix = new ParaRadix(arraySize);

        // Optional: Print the original array (commented out for large arrays)
        // System.out.println("Original Array:");
        // printArray(paraRadix.a);

        // Time the sorting process
        long startTime = System.nanoTime();
        paraRadix.findMax();
        long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1_000_000.0; // Convert nanoseconds to milliseconds

        // Optional: Print the array after sorting (commented out for large arrays)
        // System.out.println("Sorted Array:");
        // printArray(paraRadix.a);

        System.out.println("Sorting time: " + elapsedTime + " milliseconds");
    }
} // end ParaRadix
