package com.example.lock.repository;

import com.example.lock.entities.TaskQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskQueueRepository extends JpaRepository<TaskQueue, Long> {

    /**
     * 下一个任务
     * @param env
     * @param tag
     * @return
     */
    TaskQueue findFirstByEnvAndTag(String env, String tag);
}
