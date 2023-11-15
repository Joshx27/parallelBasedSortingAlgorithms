package michaeljosh;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class parallelBrickSort {
    public static void parallelBrickSortArray(long[] arr) {
        int threadNum = arr.length / 2;
        CyclicBarrier barr = new CyclicBarrier(threadNum);
        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(new CompareSwapThread(arr, 2 * i + 1, barr));
            threads[i].start();
        }
        for (int i = 0; i < threadNum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        long[] arr = {83, 71, 72, 26, 6, 81, 53, 72, 20, 35, 40, 79, 3, 90, 89, 52, 30};
        parallelBrickSortArray(arr);
        System.out.println(Arrays.toString(arr));
    }
}

class CompareSwapThread implements Runnable {
    private long[] arr;
    private int index;
    private CyclicBarrier barr;

    public CompareSwapThread(long[] arr, int index, CyclicBarrier barr) {
        this.arr = arr;
        this.index = index;
        this.barr = barr;
    }

    @Override
    public void run() {
        for (int i = 0; i < arr.length; i++) {
            if (arr[index - 1] > arr[index]) {
                long t = arr[index - 1];
                arr[index - 1] = arr[index];
                arr[index] = t;
            }
            try {
                barr.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            if (index + 1 < arr.length && arr[index] > arr[index + 1]) {
                long t = arr[index];
                arr[index] = arr[index + 1];
                arr[index + 1] = t;
            }
            try {
                barr.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}