package com.kyle.thread.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author kz37
 */
public class Student implements Runnable{

    private String name;

    private Semaphore semaphore;

    /**
     * 打饭方式
     *0    一直等待直到打到饭
     *1    等了一会不耐烦了，回宿舍吃泡面了
     *2    打饭中途被其他同学叫走了，不再等待
     */
    private Integer type;

    public Student(String name, Semaphore semaphore, Integer type) {
        this.name = name;
        this.semaphore = semaphore;
        this.type = type;
    }

    @Override
    public void run() {
        switch (type) {
            case 0:
                //一直等待直到打到饭
                semaphore.acquireUninterruptibly();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " dafan");
                semaphore.release();
                break;
            case 1:
                try {
                    // 等待超时，则不再等待，回宿舍吃泡面
                    if (semaphore.tryAcquire(10000, TimeUnit.MILLISECONDS)) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(name + " dafan");
                        semaphore.release();
                    } else {
                        System.out.println(name + "吃泡面");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    // 学生也很有耐心，但是他们班突然宣布聚餐，它只能放弃打饭了
                    semaphore.acquire();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(name + " dafan");
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println("中途有事，吃美食去了！");
                }
                break;
            default:
                break;
        }
    }
}
