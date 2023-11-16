package michaeljosh.MergeSort;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ParallelMergeSort extends Thread {
    private int threadID;
    private CyclicBarrier barrier;
    private long array[];
    private long aux[];
    private int numberOfThreads;

    public ParallelMergeSort(int threadID, CyclicBarrier barrier, long array[], long aux[],
            int numberOfThreads) {
        super("thread " + threadID);
        this.threadID = threadID;
        this.barrier = barrier;
        this.array = array;
        this.aux = aux;
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void run() {
        try {
            int blockSize = array.length / numberOfThreads;
            int first = threadID * blockSize;
            int last = first + blockSize;
            if (threadID == numberOfThreads - 1)
                last = array.length;

            // each thread sorts its sub array and waits others at the barrier
            Arrays.sort(array, first, last);
            barrier.await();

            int numberOfBlocks = numberOfThreads;

            // two threads for each pair of blocks.
            // if there are odd number of blocks,
            // last one will not be merged, so no thread is necessary for that block
            int activeThreads = (numberOfBlocks % 2 == 0) ? numberOfBlocks : numberOfBlocks - 1;

            while (numberOfBlocks > 1) {
                if (threadID < activeThreads && threadID % 2 == 0) {
                    int start = threadID * blockSize;
                    int second = start + blockSize;
                    int third = second + blockSize;
                    if (threadID + 2 == numberOfBlocks)
                        third = array.length;

                    int mergedElements = MergeSortUtil.mergeMins(array, aux, start, second, third);
                    barrier.await();
                    // copy back the merged block to the original array
                    System.arraycopy(aux, start, array, start, mergedElements);
                } else if (threadID < activeThreads && threadID % 2 != 0) {
                    int start = (threadID - 1) * blockSize;
                    int second = start + blockSize;
                    int third = second + blockSize;
                    if (threadID + 1 == numberOfBlocks)
                        third = array.length;
                    int mergedElements = MergeSortUtil.mergeMaxes(array, aux, start, second, third);
                    barrier.await();
                    // copy back the merged block to the original array
                    System.arraycopy(aux, third - mergedElements, array, third - mergedElements, mergedElements);
                } else {
                    // idle looping threads wait to synchronize
                    barrier.await();
                }
                blockSize *= 2;
                // numberOfBlocks ceiled up, since if there are odd numberOfBlocks,
                // all consecutive block pairs are merged, but the last one is not merged
                // if there are 7 blocks to merge, 6 of them are merged into 3.
                // last one stayed the same. So in total we have (3+1=4) blocks.
                numberOfBlocks = (int) Math.ceil(numberOfBlocks / 2.0);
                activeThreads = (numberOfBlocks % 2 == 0) ? numberOfBlocks : numberOfBlocks - 1;
                barrier.await();
            }

        } catch (InterruptedException ex) {
            System.out.println("exception error message: " + ex.getMessage());
            ex.printStackTrace();
        } catch (BrokenBarrierException ex) {
            System.out.println("exception error message: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * a parallel sort method that can be called from any application
     * 
     * @param array           the array to be sorted. we assume the array is full.
     * @param numberOfThreads user specifies the number of threads that will sort
     */
    public static void parallelMergeSort(long array[], int numberOfThreads) {

        long aux[] = new long[array.length];

        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads);

        ParallelMergeSort threads[] = new ParallelMergeSort[numberOfThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ParallelMergeSort(i, barrier, array, aux, numberOfThreads);
            threads[i].start();
        }

        // main thread waits for the first thread to finish.
        // it could have waited any other thread. all finish simultaneously
        try {
            threads[0].join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public static void main(String args[]) {
    }
}