package com.nikita.Dispatcher;

import com.nikita.Job.Job;
import com.nikita.Queue.QueuePriorityMap;

import java.util.Date;

public class Dispatcher implements Runnable {
    private QueuePriorityMap<Job> queuePriorityMap;
    private int executedJobs = 0;
    private int totalWaitingTime = 0;
    private int totalIdleTime = 0;
    private int totalJobs;
    private Date startedAt = new Date();

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
                        long timestamp = new Date().getTime();
                        queuePriorityMap.wait();
                        totalIdleTime += new Date().getTime() - timestamp;
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
                    System.out.printf("- Idle time: %d%n", totalIdleTime);
                    System.out.printf("- Idle time percentage: %.2f%%%n", ((double) totalIdleTime / (new Date().getTime() - startedAt.getTime())) * 100);
                    System.exit(0);
                }
            } catch (Exception err) {
                System.err.println(err);
            }
        }
    }
}
