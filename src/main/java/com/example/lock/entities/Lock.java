package com.example.lock.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

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
@Table(name = "lock_info",
        uniqueConstraints={@UniqueConstraint(columnNames={"tag"},name = "uk_tag")})
public class Lock {

    public final static Integer LOCKED_STATUS = 1;
    public final static Integer UNLOCKED_STATUS = 0;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 锁的标示，以订单为例，可以锁订单id
     */
    @Column(nullable = false)
    private String tag;

    /**
     * 过期时间
     */
    @Column(nullable = false)
    private Date expirationTime;

    /**
     * 锁状态，0，未锁，1，已经上锁
     */
    @Column(nullable = false)
    private Integer status;

    public Lock(String tag, Date expirationTime, Integer status) {
        this.tag = tag;
        this.expirationTime = expirationTime;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Lock lock = (Lock) o;
        return id != null && Objects.equals(id, lock.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
