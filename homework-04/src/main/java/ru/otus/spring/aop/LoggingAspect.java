package ru.otus.spring.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LogManager.getLogger();

    @Before("@annotation(ru.otus.spring.aop.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Start method: {}, with args: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }
}
