package ru.otus.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LogManager.getLogger("LOGFILE");

    @Before("@target(org.springframework.stereotype.Service)")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Start method: " + joinPoint.getSignature().getName());
    }
}
