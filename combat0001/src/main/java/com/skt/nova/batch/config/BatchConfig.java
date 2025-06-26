package com.skt.nova.batch.config;

import com.skt.nova.batch.common.listener.NovaJobExecutionListener;
import com.skt.nova.batch.common.listener.NovaStepExecutionListener;
import com.skt.nova.batch.step001.SimpleTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Tasklet
 * Use Case: Best suited for simple repetitive jobs or repeatedly invoking a single online module.
 */
@Configuration
public class BatchConfig {

    @Value("${spring.batch.job.name}")
    private String batchJobName;

    @Bean
    public Step step001(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager,
                        SimpleTasklet simpleTasklet,
                        NovaStepExecutionListener listener
    ) {
        return new StepBuilder("step001", jobRepository)
                .tasklet(simpleTasklet, transactionManager)
                .listener(listener)
                .build();
    }

    @Bean
    public Job combat0001(JobRepository jobRepository,
                       Step step001,
                       NovaJobExecutionListener listener
    ) {
        return new JobBuilder(batchJobName, jobRepository)
                .start(step001)
                .listener(listener)
                .build();
    }
}
