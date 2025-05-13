/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._2251172562_phamquyvu_thuchanh2;

/**
 *
 * @author Admin
 */
import java.util.Random;

public class ThucHanh1 {
    static final int N = 10; // Số phần tử trong mảng
    static final int NUM_THREADS = 3; // Số luồng
    static int[] A = new int[N];
    static int[] maxValues = new int[NUM_THREADS];

    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        
        // 1. Sinh ngẫu nhiên mảng A
        for (int i = 0; i < N; i++) {
            A[i] = rand.nextInt(1000); // [0, 999]
        }
        A[N-1]=2000;

        // 2. Tạo và chạy các luồng
        Thread[] threads = new Thread[NUM_THREADS];
        int chunkSize = N / NUM_THREADS;

        long start = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int startIdx = threadId * chunkSize;
                int endIdx = (threadId == NUM_THREADS - 1) ? N : startIdx + chunkSize;

                int localMax = A[startIdx];
                for (int j = startIdx + 1; j < endIdx; j++) {
                    if (A[j] > localMax) {
                        localMax = A[j];
                    }
                }

                maxValues[threadId] = localMax;
                System.out.println("Luong " + threadId + " tim max = " + localMax);
            });
            threads[i].start();
        }

        // 3. Chờ tất cả luồng kết thúc
       for (Thread t : threads) {
            t.join();
        }

//        4. Tổng hợp kết quả max toàn cục
        int globalMax = maxValues[0];
        for (int i = 1; i < NUM_THREADS; i++) {
            if (maxValues[i] > globalMax) {
                globalMax = maxValues[i];
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Phan tu lon nhat trong mang: " + globalMax);
        System.out.println("Thoi gian xu ly: " + (end - start) + " ms");
    }
}
