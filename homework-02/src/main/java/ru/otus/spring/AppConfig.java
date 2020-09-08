package ru.otus.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@EnableAspectJAutoProxy
@ComponentScan
@Configuration
@PropertySource(value = "classpath:application.properties")
public class AppConfig {

}
