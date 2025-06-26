package com.skt.nova.batch.common.listener;

import com.skt.nova.batch.common.NovaBatchProperties;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * {@code NovaJobExecutionListener} is a custom implementation of Spring Batch's
 * {@link JobExecutionListener} to provide pre- and post-execution hooks for batch jobs.
 *
 * <p>
 * This component:
 * <ul>
 *     <li>Logs the job start and end times with localized formatting</li>
 *     <li>Prints the total write count from all steps</li>
 *     <li>Displays elapsed execution time and job mode (virtual vs thread pool)</li>
 *     <li>Reads runtime settings like chunk size from {@link NovaBatchProperties}</li>
 * </ul>
 *
 * <p>
 * It is automatically picked up as a Spring component due to the {@link Component} annotation.
 * </p>
 */
@Component
public class NovaJobExecutionListener implements JobExecutionListener {

    private final NovaBatchProperties properties;
    private long startTime;

    private static final Logger logger = LoggerFactory.getLogger(NovaJobExecutionListener.class);

    /**
     * Constructs a new instance of the listener with injected {@link NovaBatchProperties}.
     *
     * @param properties configuration source for chunk size and thread settings
     */
    public NovaJobExecutionListener(NovaBatchProperties properties) {
        this.properties = properties;
    }

    /**
     * Invoked right before the job starts. Stores the start timestamp and logs the job name.
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {

        startTime = System.currentTimeMillis();
        logger.info("\uD83D\uDFE1 Job Started: {}", jobExecution.getJobInstance().getJobName());
    }

    /**
     * Invoked after the job ends. Logs detailed metrics including:
     * <ul>
     *     <li>Execution mode (virtual thread or thread pool)</li>
     *     <li>Start and end timestamps</li>
     *     <li>Elapsed time in a human-readable format</li>
     *     <li>Chunk size and total write count</li>
     * </ul>
     *
     * @param jobExecution job context provided by Spring Batch
     */
    @Override
    public void afterJob(@Nullable JobExecution jobExecution) {

        Objects.requireNonNull(jobExecution, "JobExecution must not be null");

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // Print execution mode based on configuration
        if (properties.isUseVirtualThread()) {
            logger.info("🧵 Thread Mode: Virtual threads");
        }
        else {
            if (properties.isUsePartitioner()) {
                logger.info("🧵 Thread Mode: Multithreaded");
            }
            else {
                logger.info("🧵 Thread Mode: Single-threaded");
            }
        }

        // Aggregate total write count from all step executions
        long writeCount = jobExecution.getStepExecutions()
                .stream()
                .mapToLong(StepExecution::getWriteCount)
                .sum();

        // Log job metadata and performance info
        logger.info("\uD83D\uDFE2 Job Ended: {}", jobExecution.getJobInstance().getJobName());
        logger.info("   - Start time   : {}", formatKoreanTimestamp(startTime));
        logger.info("   - End time     : {}", formatKoreanTimestamp(endTime));
        logger.info("   - Elapsed Time : {}", formatElapsedTime(elapsed));

        logger.info("🧪 Execution information");
        logger.info("   - chunk-size   : {}", String.format("%,d", properties.getChunkSize()));

        logger.info("\uD83E\uDDEE Processed count: {}", String.format("%,d", writeCount));
    }

    /**
     * Formats the elapsed milliseconds into a human-readable time format:
     * "Xd XXh XXm XX.XXXs"
     *
     * @param elapsedMillis total time in milliseconds
     * @return formatted time string
     */
    private static String formatElapsedTime(long elapsedMillis) {
        long millis = elapsedMillis % 1000;
        long totalSeconds = elapsedMillis / 1000;

        long seconds = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;

        long minutes = totalMinutes % 60;
        long totalHours = totalMinutes / 60;

        long hours = totalHours % 24;
        long days = totalHours / 24;

        return String.format("%dd %02dh %02dm %02d.%03ds", days, hours, minutes, seconds, millis);
    }

    /**
     * Converts an epoch millisecond timestamp into a Korean-localized formatted date-time string.
     *
     * @param epochMillis timestamp in milliseconds
     * @return formatted date-time string (yyyy년 MM월 dd일 HH:mm:ss.SSS)
     */
    public static String formatKoreanTimestamp(long epochMillis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                ZoneId.systemDefault()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return dateTime.format(formatter);
    }
}
