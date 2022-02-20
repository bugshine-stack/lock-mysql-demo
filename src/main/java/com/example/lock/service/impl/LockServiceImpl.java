package com.example.lock.service.impl;

import com.example.lock.entities.Lock;
import com.example.lock.repository.LockRepository;
import com.example.lock.service.LockService;
import io.micrometer.core.instrument.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author kongxiangshuai
 */
@Service
public class LockServiceImpl implements LockService {

    private final Integer DEFAULT_EXPIRED_SECONDS = 10;

    private final LockRepository lockRepository;

    public LockServiceImpl(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
    }

    @Override
    @Transactional(rollbackOn = Throwable.class)
    public boolean tryLock(String tag, Integer expiredSeconds) {
        if (StringUtils.isEmpty(tag)) {
            throw new NullPointerException();
        }
        Lock lock = lockRepository.findByTag(tag);
        if (Objects.isNull(lock)) {
            lockRepository.save(new Lock(tag, this.addSeconds(new Date(), expiredSeconds), Lock.LOCKED_STATUS));
            return true;
        } else {
            Date expiredTime = lock.getExpirationTime();
            Date now = new Date();
            if (expiredTime.before(now)) {
                lock.setExpirationTime(this.addSeconds(now, expiredSeconds));
                lockRepository.save(lock);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackOn = Throwable.class)
    public void unlock(String tag) {
        if (StringUtils.isEmpty(tag)) {
            throw new NullPointerException();
        }
        lockRepository.deleteByTag(tag);
    }

    private Date addSeconds(Date date, Integer seconds) {
        if (Objects.isNull(seconds)){
            seconds = DEFAULT_EXPIRED_SECONDS;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}