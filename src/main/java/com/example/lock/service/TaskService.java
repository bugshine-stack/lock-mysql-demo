package com.example.lock.service;

import com.example.lock.entities.TaskQueue;
import com.example.lock.repository.TaskQueueRepository;
import com.example.lock.util.ProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author kongxiangshuai
 */
@Slf4j
@Service
public abstract class TaskService {

    @Autowired
    private LockService lockService;

    @Autowired
    private TaskQueueRepository taskQueueRepository;

    private Map<String, AtomicBoolean> taskHasSchedule = new ConcurrentHashMap<>();

    @Autowired
    private ProfileUtil profileUtil;

    protected void addTask(String tag, Integer expiredSeconds, Long taskId) {
        TaskQueue taskQueue = new TaskQueue();
        taskQueue.setTag(tag);
        taskQueue.setEnv(profileUtil.getActiveProfile());
        taskQueue.setGmtTime(new Date());
        taskQueue.setTaskId(taskId);
        taskQueueRepository.save(taskQueue);
        startTask(tag, expiredSeconds);
    }

    private void startTask(String tag, Integer expiredSeconds) {
        if (!this.taskHasSchedule.containsKey(tag)) {
            this.taskHasSchedule.put(tag, new AtomicBoolean(false));
        }

        AtomicBoolean atomicBoolean = this.taskHasSchedule.get(tag);
        if (atomicBoolean.get()) {
            // 已经在执行的跳过，避免重复调用
            return;
        }
        atomicBoolean.set(true);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> runTask(tag, expiredSeconds));
        atomicBoolean.set(false);
    }

    private void runTask(String tag, Integer expiredSeconds) {
        TaskQueue task = taskQueueRepository.findFirstByEnvAndTag(profileUtil.getActiveProfile(), tag);
        if (task == null) {
            log.info("task not exists ... exit.");
        } else {
            log.info("start task {}", task);
            String lockTag = String.format("%s_%s", tag, getResourceTag(task.getTaskId()));
            if (lockService.tryLock(lockTag, expiredSeconds)) {
                execute(task.getTaskId());
                taskQueueRepository.delete(task);
                lockService.unlock(lockTag);
            }
            runTask(tag, expiredSeconds);
        }
    }

    /**
     * @param taskId 任务唯一标识
     * @return
     */
    protected String getResourceTag(Long taskId) {
        return taskId.toString();
    };

    /**
     * 执行逻辑
     * @param taskId 任务ID
     */
    protected abstract void execute(Long taskId);

}
