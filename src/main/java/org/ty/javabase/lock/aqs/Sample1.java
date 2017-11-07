package org.ty.javabase.lock.aqs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <dl>
 * <dt>org.ty.Lock.AQS</dt>
 * <dd>Description: 重入锁ReentrantLock</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/5/16</dd>
 * </dl>
 *
 * @author tangyun
 */
public class Sample1 {

    public static void main(String[] args) throws InterruptedException{
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition condition = reentrantLock.newCondition();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    System.out.println("我们都能进入此进程"+this);
                    System.out.println("我要等待一个新信号"+this);
                    condition.await();//释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("拿到一个信号!!"+this);
                reentrantLock.unlock();
            }
        },"waitThread1");
        thread.start();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    System.out.println("我们都能进入此进程"+this);
                    System.out.println("我拿到锁了！！"+this);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                condition.signalAll();
                System.out.println("我发送了一个信号"+this);
                reentrantLock.unlock();
            }
        },"singalThread");
        thread1.start();

    }
}
