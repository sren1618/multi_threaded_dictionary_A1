package org.ds.a1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * ClassName: MyThreadsPool
 * Package: org.ds.a1
 * @Author Shiqiang Ren
 * @StudentID 1348968
 * @Create 1/4/2023 4:15 pm
 * @Version 1.0
 */
public class MyThreadsPool {
    private final int maxPoolSize;
    private final int initPoolSize;
    private final int workerAliveTime;
    private Vector<Thread> workers;
    private int workersNumber;
    private final Queue<Runnable> tasks = new LinkedList<>();

    public MyThreadsPool(int initPoolSize, int maxPoolSize, int workerAliveTime) {
        this.initPoolSize = initPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.workerAliveTime = workerAliveTime;
        workers = new Vector<>();
        for (int i = 0; i < initPoolSize; i++) {
            addWorkerToPool();
            workers.get(i).start();
        }
        if (workers.size() == initPoolSize) {
            workersNumber = initPoolSize;
            System.out.println("Init Workers Done!");
            System.out.println("Threads pool has created!");
        } else {
            System.out.println("Init Workers Failed!");
        }
    }

    public void addWorkerToPool() {
        workers.add(new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Runnable taskInQ;
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            try {
                                tasks.wait(workerAliveTime);
                                if (tasks.isEmpty() && workersNumber > initPoolSize) {
                                    return;
                                }
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        taskInQ = tasks.poll();
                    }
                    taskInQ.run();
                }
            }
        }));
    }

    public void execute(Runnable task) {
        checkActiveWorkersNumber();
        synchronized (tasks) {
            tasks.add(task);
            tasks.notifyAll();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (tasks.size() > 0 && workersNumber < maxPoolSize) {
            addWorkerToPool();
            workers.get(workersNumber).start();
            workersNumber = workersNumber + 1;
            System.out.println("New worker added! --- current workers: " + workersNumber);
        }
    }

    public void checkActiveWorkersNumber() {
        Vector<Thread> newWorkers = new Vector<>();
        for (Thread worker : workers) {
            Thread.State state = worker.getState();
            if (state != Thread.State.TERMINATED) {
                newWorkers.add(worker);
            }
        }
        workers = newWorkers;
        workersNumber = workers.size();
    }

    public void closePool() {
        synchronized (tasks) {
            if (workers.size() > 0) {
                for (Thread worker : workers) {
                    if (!worker.isInterrupted()) {
                        worker.interrupt();
                    }
                }
            } else {
                System.out.println("No active worker now");
            }
        }
    }

}