package com.github.sergjei.restaurant_voting.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheInvalidator {
    private static Logger log = LogManager.getLogger(CacheInvalidator.class);

    @Scheduled(cron = "0 0 0 * * * ")
    @CacheEvict(value = "menu", allEntries = true)
    public void clearCache() {
        log.debug("Cache 'menu' cleared by invalidator");
    }
}
