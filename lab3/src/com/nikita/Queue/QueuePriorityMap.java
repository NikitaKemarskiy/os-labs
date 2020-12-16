package com.nikita.Queue;

import java.util.HashMap;
import java.util.Map;

public class QueuePriorityMap<T> {
    private int minPriority;
    private int maxPriority;
    private Map<Integer, Queue<T>> queuePriorityMap;

    public QueuePriorityMap(int minPriority, int maxPriority) {
        this.minPriority = minPriority;
        this.maxPriority = maxPriority;
        queuePriorityMap = new HashMap<>();
        for (int priority = minPriority; priority < maxPriority; priority++) {
            queuePriorityMap.put(priority, new Queue(priority));
        }
    }

    public T pop() {
        for (int priority = minPriority; priority < maxPriority; priority++) {
            Queue<T> queue = queuePriorityMap.get(priority);
            if (!queue.isEmpty()) {
                return queue.pop();
            }
        }
        return null;
    }

    public void push(T item, int priority) {
        queuePriorityMap.get(priority).push(item);
    }

    public int getMinPriority() {
        return minPriority;
    }

    public int getMaxPriority() {
        return maxPriority;
    }
}
