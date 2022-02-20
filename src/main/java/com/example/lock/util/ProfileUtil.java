package com.example.lock.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取profile
 * @author kongxiangshuai
 */
@Component
@Slf4j
public class ProfileUtil implements ApplicationContextAware {

    private ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public String getActiveProfile() {
        log.info("profile {}", context.getEnvironment());
        String[] profiles = context.getEnvironment().getActiveProfiles();
        if (profiles.length > 0) {
            return profiles[0];
        }
        return "";
    }
}
