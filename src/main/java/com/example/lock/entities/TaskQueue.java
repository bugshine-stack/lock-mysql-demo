package com.example.lock.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * @author kongxiangshuai
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "task_queue")
public class TaskQueue {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 任务类别
     */
    @Column(nullable = false)
    private String tag;

    /**
     * 过期时间
     */
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtTime;

    /**
     * 环境
     */
    private String env;

    /**
     * 任务详情
     */
    @Column(nullable = false)
    private Long taskId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaskQueue taskQueue = (TaskQueue) o;
        return id != null && Objects.equals(id, taskQueue.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
