package com.skt.nova.batch.config;

import com.skt.nova.batch.common.NovaBatchProperties;
import com.skt.nova.batch.common.listener.NovaJobExecutionListener;
import com.skt.nova.batch.common.listener.NovaStepExecutionListener;
import com.skt.nova.batch.common.item.NovaItemReaderBuilder;
import com.skt.nova.batch.entity.Invoice;
import com.skt.nova.batch.entity.InvoiceRowMapper;
import com.skt.nova.batch.step001.InvoiceProcessor;
import com.skt.nova.batch.step001.InvoiceWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Single Chunk-Oriented Processing
 * Use Case: Used when retrieving multiple records and processing them iteratively.
 * The standard approach is to use NovaItemReader and NovaItemWriter provided by the NOVA common module.
 * Similar to the Spring Batch framework, ItemProcessor is optional and can be used when necessary.
 */
@Configuration
public class BatchConfig {

    private final NovaBatchProperties properties;
    private final DataSource dataSource;

    @Value("${spring.batch.job.name}")
    private String batchJobName;

    public BatchConfig(
            NovaBatchProperties properties,
            DataSource dataSource
    ) {
        this.properties = properties;
        this.dataSource = dataSource;
    }

    @Bean
    public Job combat0002(JobRepository jobRepository,
                          Step step001,
                          NovaJobExecutionListener listener
    ) {
        return new JobBuilder(batchJobName, jobRepository)
                .start(step001)
                .listener(listener)
                .build();
    }

    @Bean
    public Step step001(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager,
                        InvoiceProcessor processor,
                        InvoiceWriter writer,
                        NovaStepExecutionListener listener,
                        TaskExecutor taskExecutor
    ) {

        return new StepBuilder("step001", jobRepository)
                .<Invoice, Invoice>chunk(properties.getChunkSize(), transactionManager)
                .reader(new NovaItemReaderBuilder<Invoice>()
                        .readerType(NovaItemReaderBuilder.ReaderType.JDBC_CURSOR)
                        .fetchSize(properties.getChunkSize())
                        .dataSource(dataSource)
                        .sql("SELECT * FROM billing.invoice")
                        .rowMapper(new InvoiceRowMapper())
                        .build())
                .processor(processor)
                .writer(writer)
                .listener(listener)
                .taskExecutor(taskExecutor)
                .build();
    }
}
