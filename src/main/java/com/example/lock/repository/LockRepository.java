package com.example.lock.repository;

import com.example.lock.entities.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kongxiangshuai
 */
public interface LockRepository extends JpaRepository<Lock, Long> {

    /**
     * 根据锁标识查找
     * @param tag 标识
     * @return Lock
     */
    Lock findByTag(String tag);

    /**
     * 删除锁
     * @param tag
     */
    void deleteByTag(String tag);
}
