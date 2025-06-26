package com.skt.nova.batch.common.executor;

import com.skt.nova.batch.common.NovaBatchProperties;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configures the TaskExecutor used by Spring Batch.
 * <p>Supports three execution modes:</p>
 * <ol>
 *  <li>Single-threaded (SyncTaskExecutor)</li>
 *  <li>Multithreaded with ThreadPoolTaskExecutor</li>
 *  <li>Virtual Threads (via ExecutorService)</li>
 * </ol>
 */
@Configuration
public class NovaTaskExecutorConfig {

    private final NovaBatchProperties properties;
    private ExecutorService virtualExecutor; // ExecutorService for virtual thread mode (Java 21+)

    private static final Logger logger = LoggerFactory.getLogger(NovaTaskExecutorConfig.class);

    public NovaTaskExecutorConfig(NovaBatchProperties properties) {
        this.properties = properties;
    }

    /**
     * Primary TaskExecutor bean that switches behavior based on partitioning and virtual thread configuration.
     */
    @Bean
    @Primary
    public TaskExecutor resolveTaskExecutor() {

        if (!properties.isUsePartitioner()) {
            return useSingleThreadMode();
        }

        return properties.isUseVirtualThread()
                ? useVirtualThreadMode()
                : useMultithreadMode();
    }

    private TaskExecutor useSingleThreadMode() {
        logger.info("🧵 Thread Mode: Single-threaded");
        return new SyncTaskExecutor();
    }

    private TaskExecutor useMultithreadMode() {
        logger.info("🧵 Thread Mode: Multithreaded");
        return getThreadPoolTaskExecutor();
    }

    private TaskExecutor useVirtualThreadMode() {
        logger.info("🧵 Thread Mode: Virtual threads");
        if (virtualExecutor == null) {
            virtualExecutor = Executors.newThreadPerTaskExecutor(
                    Thread.ofVirtual().name("vth-", 0).factory()
            );
        }
        return new TaskExecutorAdapter(virtualExecutor);
    }

    /**
     * Configures a traditional thread pool for parallel execution
     * using values from BatchProperties.
     */
    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(properties.getNormalThreadPoolMinSize());
        executor.setMaxPoolSize(properties.getNormalThreadPoolMaxSize());
        executor.setQueueCapacity(properties.getNormalThreadPoolQueueCapacity());
        executor.setThreadNamePrefix("th-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    /**
     * Clean up virtual thread executor when the container shuts down.
     */
    @PreDestroy
    public void shutdown() {
        if(virtualExecutor != null && !virtualExecutor.isShutdown()) {
            virtualExecutor.shutdown();
        }
    }
}
