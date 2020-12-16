package com.nikita;

import com.nikita.Dispatcher.Dispatcher;
import com.nikita.Job.Job;
import com.nikita.Job.JobImitator;
import com.nikita.Queue.QueuePriorityMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties config = getConfig();
        QueuePriorityMap<Job> queuePriorityMap = new QueuePriorityMap<Job>(
            Integer.parseInt(config.getProperty("priority.min")),
            Integer.parseInt(config.getProperty("priority.max"))
        );

        Thread dispatcherThread = getDispatcherThread(queuePriorityMap);
        dispatcherThread.start();

        Thread jobImitatorThread = getJobImitatorThread(queuePriorityMap, config);
        jobImitatorThread.start();
    }

    private static Properties getConfig() {
        Properties config = new Properties();
        try (InputStream inputStream = new FileInputStream("config/config.properties")) {
            config.load(inputStream);
        } catch (IOException err) {
            System.err.println(err);
            System.exit(1);
        }

        return config;
    }

    private static Thread getDispatcherThread(QueuePriorityMap queuePriorityMap) {
        Dispatcher dispatcher = new Dispatcher(queuePriorityMap);

        return new Thread(dispatcher);
    }

    private static Thread getJobImitatorThread(
        QueuePriorityMap<Job> queuePriorityMap,
        Properties config
    ) {
        JobImitator jobImitator = new JobImitator(
            queuePriorityMap,
            Integer.parseInt(config.getProperty("job.minExecutionTime")),
            Integer.parseInt(config.getProperty("job.maxExecutionTime")),
            Integer.parseInt(config.getProperty("imitator.minNextJobInterval")),
            Integer.parseInt(config.getProperty("imitator.maxNextJobInterval")),
            Integer.parseInt(config.getProperty("imitator.totalJobs"))
        );

        return new Thread(jobImitator);
    }
}
