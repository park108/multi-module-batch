package com.skt.nova.batch.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * {@code NovaStepExecutionListener} is a simple listener that logs
 * the start and end of each Step within a Spring Batch job.
 *
 * <p>
 * This component helps track the flow of Step execution,
 * making it easier to observe and debug the batch process.
 * </p>
 */
@Component
public class NovaStepExecutionListener implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(NovaStepExecutionListener.class);

    /**
     * Called just before the Step starts.
     * Logs the name of the step.
     *
     * @param stepExecution the context for the step about to be executed
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("\uD83D\uDFE1 Step Started: {}", stepExecution.getStepName());
    }

    /**
     * Called immediately after the Step has finished.
     * Logs the step's execution status.
     *
     * @param stepExecution the context for the completed step
     * @return the {@link ExitStatus} of the step, used by the Job to decide flow
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("\uD83D\uDFE2 Step Ended: {}", stepExecution.getStatus());
        return stepExecution.getExitStatus();
    }
}
