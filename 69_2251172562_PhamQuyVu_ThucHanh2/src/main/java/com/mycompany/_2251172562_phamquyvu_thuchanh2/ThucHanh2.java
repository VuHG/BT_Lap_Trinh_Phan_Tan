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
import java.util.concurrent.Semaphore;

public class ThucHanh2 {
    private final int BUFFER_SIZE = 10; // kích thước vùng đệm
    private final int[] buffer = new int[BUFFER_SIZE];
    private int in = 0; // con trỏ ghi vào buffer
    private int out = 0; // con trỏ đọc ra từ buffer

    private final int K = 2; // số luồng sinh dữ liệu
    private final int H = 2; // số luồng xử lý dữ liệu

    private final Semaphore emptySlots = new Semaphore(BUFFER_SIZE); // semaphore cho slot trống
    private final Semaphore filledSlots = new Semaphore(0);          // semaphore cho slot đầy
    private final Semaphore mutex = new Semaphore(1);                // semaphore bảo vệ vùng đệm
    private final Random rand = new Random();

    public static void main(String[] args) {
        new ThucHanh2().start();
    }

    public void start() {
        for (int i = 0; i < K; i++) {
            int id = i;
            new Thread(() -> produce(id)).start();
        }

        for (int i = 0; i < H; i++) {
            int id = i;
            new Thread(() -> consume(id)).start();
        }
    }

    private void produce(int id) {
        while (true) {
            try {
                int value = rand.nextInt(1000);

                emptySlots.acquire(); // chờ nếu không còn chỗ trống
                mutex.acquire(); // chỉ 1 luồng truy cập buffer

                buffer[in] = value;
                in = (in + 1) % BUFFER_SIZE;

                System.out.println("P" + id + ": " + value + " - " + System.currentTimeMillis());

                mutex.release(); // thả quyền truy cập buffer
                filledSlots.release(); // tăng số lượng slot đã đầy

                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consume(int id) {
        while (true) {
            try {
                filledSlots.acquire(); // chờ nếu không có dữ liệu
                mutex.acquire(); // chỉ 1 luồng truy cập buffer

                int value = buffer[out];
                out = (out + 1) % BUFFER_SIZE;

                mutex.release(); // thả quyền truy cập buffer
                emptySlots.release(); // tăng số lượng slot trống

                int result = value * value;
                System.out.println("C" + id + ": " + value + " - " + result + " - " + System.currentTimeMillis());

                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
