package org.ty.javabase.synchronizeds.sample160001.main;


/**
 * <dl>
 * <dt>org.ty.synchronizeds</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/5/11</dd>
 * </dl>
 *
 * @author tangyun
 */
public class TestMain implements Runnable {

    private String user;

    public TestMain(String user){
        this.user = user;
    }

    public static void main(String[] args) {
        TestMain testMain = new TestMain("Thread-A");
        TestMain testMainB = new TestMain("Thread-A");
        TestMain testMainC = new TestMain("Thread-C");
        Thread thread1 = new Thread(testMain);
        Thread thread2 = new Thread(testMainB);
        Thread thread3 = new Thread(testMainC);
        thread1.start();
        thread2.start();
        thread3.start();
    }

    @Override
    public void run() {
        DoSomeThing doSomeThing = DoSomeThing.getInstance();
        doSomeThing.doSome(user);
    }
}
