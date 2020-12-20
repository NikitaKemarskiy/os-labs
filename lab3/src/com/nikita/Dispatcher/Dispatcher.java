package com.nikita.Dispatcher;

import com.nikita.Job.Job;
import com.nikita.Queue.QueuePriorityMap;

public class Dispatcher implements Runnable {
    private QueuePriorityMap<Job> queuePriorityMap;
    private int executedJobs = 0;
    private int totalWaitingTime = 0;
    private int totalJobs;

    public Dispatcher(
        QueuePriorityMap<Job> queuePriorityMap,
        int totalJobs
    ) {
        this.queuePriorityMap = queuePriorityMap;
        this.totalJobs = totalJobs;
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

                executedJobs++;
                totalWaitingTime += job.getWaitingTime();

                if (executedJobs == totalJobs) {
                    System.out.println("===================");
                    System.out.printf("- Jobs executed: %d%n", executedJobs);
                    System.out.printf("- Average waiting time: %d%n", totalWaitingTime / executedJobs);
                    System.exit(0);
                }
            } catch (Exception err) {
                System.err.println(err);
            }
        }
    }
}
