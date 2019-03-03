package com.salesmanager.core.business.configuration;

import java.util.concurrent.Executor;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.salesmanager.core.model"})
@ComponentScan({"com.salesmanager.core.business.services","com.salesmanager.core.business.utils"})
@ImportResource("classpath:/spring/shopizer-core-context.xml")
public class CoreApplicationConfiguration {
	
	@Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("EmailService-");
        executor.initialize();
        System.out.println("Executor object "+executor);
        return executor;	
    }

}
