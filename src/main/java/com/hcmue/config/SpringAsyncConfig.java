package com.hcmue.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

	@Bean(name = "ocrThread-")
	public Executor threadPoolTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

	@Bean(name = "threadPoolTaskExecutorForVerifyEmail")
	public TaskExecutor getTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(2);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		return threadPoolTaskExecutor;
	}
	
	@Bean(name = "threadPoolTaskExecutorForResetPasswordEmail")
	public TaskExecutor getTaskRessetExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(2);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		return threadPoolTaskExecutor;
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(10);
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
}
