package com.nikita.Job;

import com.nikita.Dispatcher.Dispatcher;
import com.nikita.Queue.QueuePriorityMap;
import com.nikita.Util.Util;

public class JobImitator implements Runnable {
    private QueuePriorityMap<Job> queuePriorityMap;
    private int minExecutionTime;
    private int maxExecutionTime;
    private int minNextJobInterval;
    private int maxNextJobInterval;
    private int totalJobs;

    public JobImitator(
        QueuePriorityMap<Job> queuePriorityMap,
        int minExecutionTime,
        int maxExecutionTime,
        int minNextJobInterval,
        int maxNextJobInterval,
        int totalJobs
    ) {
        this.queuePriorityMap = queuePriorityMap;
        this.minExecutionTime = minExecutionTime;
        this.maxExecutionTime = maxExecutionTime;
        this.minNextJobInterval = minNextJobInterval;
        this.maxNextJobInterval = maxNextJobInterval;
        this.totalJobs = totalJobs;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalJobs; i++) {
            int priority = Util.randomInt(queuePriorityMap.getMinPriority(), queuePriorityMap.getMaxPriority());
            Job job = new Job(Util.randomInt(minExecutionTime, maxExecutionTime), priority);
            synchronized (queuePriorityMap) {
                queuePriorityMap.push(job, priority);
                queuePriorityMap.notify();
            }
            try {
                Thread.sleep(Util.randomInt(minNextJobInterval, maxNextJobInterval));
            } catch (InterruptedException err) {
                System.err.println(err);
            }
        }
    }
}
