package com.example.lock.service.impl;

import com.example.lock.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kongxiangshuai
 */
@Service
@Slf4j
public class EnquiryTaskService extends TaskService {

    private final Random random = new Random(5);
    private final String taskTag = "enquiry";
    private final Integer expiredSeconds = 600;
    /**
     *  模拟自增ID
     */
    private final AtomicLong id = new AtomicLong();

    public void createTask(String msg) {
        log.info("create task msg ... {}", msg);
        addTask(taskTag, expiredSeconds, id.incrementAndGet());
    }

    @Override
    protected void execute(Long taskId) {
        int executeTime = random.nextInt(5) + 10;
        try {
            Thread.sleep(executeTime * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("execute enquiry task complete ... [%s -> %s]", taskId, executeTime);
    }

}
