package com.nikita.Job;

import java.util.Date;

public class Job {
    public int id;
    public int executionTime;
    public int priority;
    public long createdAt;
    public long waitingTime;

    private static int currentId = 1;

    public Job(int executionTime, int priority) {
        this.id = currentId++;
        this.executionTime = executionTime;
        this.priority = priority;
        this.createdAt = (new Date().getTime());
        System.out.printf(">>> Job [%d] created%n", id);
    }

    public void execute() throws InterruptedException {
        waitingTime = (new Date().getTime()) - createdAt;
        System.out.printf(">>> Start executing Job [%d] with priority %d. Waiting time: %d%n", id, priority, waitingTime);
        Thread.sleep(executionTime);
        System.out.printf(">>> Job [%d] executed. Execution time: %d%n", id, executionTime);
    }

    public long getWaitingTime() {
        return waitingTime;
    }
}
