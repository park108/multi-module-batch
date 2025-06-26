package com.skt.nova.batch.step001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SimpleTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTasklet.class);

    private static final String OFFSET_KEY = "step0001.offset";

    @Override
    public RepeatStatus execute(@Nullable StepContribution contribution, ChunkContext chunkContext) {


        // Get current data in chunk context
        // Note: ExecutionContext manages its data using a ConcurrentHashMap to ensure thread-safety in
        // a multithreaded environment.
        ExecutionContext stepContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

        int offset = stepContext.containsKey(OFFSET_KEY) ? stepContext.getInt(OFFSET_KEY) : 0;
        logger.debug("   - Current offset = {}", offset);

        // Business logic
        logger.debug("   - Generate UUID = {}", UUID.randomUUID());
        logger.debug("   - 2 powers {} = {}", offset, Math.pow(2, offset));

        // Set next offset
        stepContext.putInt(OFFSET_KEY, ++offset);

        // Processing CONTINUABLE
        if (offset < 100) {
            return RepeatStatus.CONTINUABLE;
        }

        // Processing FINISHED
        else {
            return RepeatStatus.FINISHED;
        }
    }
}
