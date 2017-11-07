package org.ty.javabase.synchronizeds;

/**
 * <dl>
 * <dt>org.ty.javabase</dt>
 * <dd>Description: synchronized 示例</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/7</dd>
 * </dl>
 *
 * @author tangyun
 */
public class SynchronizedSample implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " load in");
        synchronized (this){
            for (int i=0;i<5;i++){
                System.out.println(Thread.currentThread().getName() + " synchronized loop " + i);
            }
        }
    }

    /**
     * Java语言的关键字，当它用来修饰一个方法或者一个代码块的时候，能够保证在同一时刻最多只有一个线程执行该段代码。
     * 当一个线程访问object的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该object中的非synchronized(this)同步代码块
     * 尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。     *
     * @param args
     */
    public static void main(String[] args) {
        SynchronizedSample synchronizedSample = new SynchronizedSample();
        Thread ta = new Thread(synchronizedSample,"A");
        Thread tb = new Thread(synchronizedSample,"B");
        ta.start();
        tb.start();
    }
}
