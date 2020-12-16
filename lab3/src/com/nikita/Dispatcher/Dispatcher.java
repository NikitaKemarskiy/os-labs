package com.nikita.Dispatcher;

import com.nikita.Job.Job;
import com.nikita.Queue.QueuePriorityMap;

public class Dispatcher implements Runnable {
    private QueuePriorityMap<Job> queuePriorityMap;

    public Dispatcher(QueuePriorityMap<Job> queuePriorityMap) {
        this.queuePriorityMap = queuePriorityMap;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Job job;
                synchronized (queuePriorityMap) {
                    job = queuePriorityMap.pop();
                    if (job == null) {
                        queuePriorityMap.wait();
                        continue;
                    }
                }
                job.execute();
            } catch (Exception err) {
                System.err.println(err);
            }
        }
    }
}
