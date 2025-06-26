package com.skt.nova.batch.common.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * {@code NovaBatchJobRunner} is a Spring Boot component that executes a Spring Batch Job
 * automatically at application startup.
 *
 * <p>
 * It implements {@link CommandLineRunner}, so the {@code run()} method is invoked
 * right after the application context is fully initialized.
 * </p>
 *
 * <p>
 * The runner expects a job ID to be passed via command-line argument, e.g.:
 * <pre>
 * java -jar nova-batch-job0001.jar job=sampleJob
 * </pre>
 * </p>
 *
 * <p>
 * It retrieves the {@link Job} from the {@link JobRegistry} using the provided job ID,
 * constructs appropriate {@link JobParameters}, and launches the job using {@link JobLauncher}.
 * </p>
 */
@Component
public class NovaBatchJobRunner implements CommandLineRunner {

    private static final String PARAM_TIMESTAMP = "timestamp";
    private static final String PARAM_JOB_NAME = "jobName";

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    private static final Logger logger = LoggerFactory.getLogger(NovaBatchJobRunner.class);

    @Value("${spring.batch.job.name}")
    private String batchJobName;

    /**
     * Constructs a new {@code NovaBatchJobRunner} with required dependencies.
     *
     * @param jobLauncher the Spring Batch JobLauncher to trigger job execution
     * @param jobRegistry the registry used to retrieve jobs by name
     */
    public NovaBatchJobRunner(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    /**
     * Entry point automatically invoked by Spring Boot after context startup.
     * <p>
     * This method:
     * <ol>
     *     <li>Parses command-line arguments to extract {@code job}</li>
     *     <li>Creates a {@link JobParameters} instance with a timestamp and job ID</li>
     *     <li>Looks up the job in the {@link JobRegistry}</li>
     *     <li>Launches the job using {@link JobLauncher}</li>
     *     <li>Logs the status of job execution</li>
     * </ol>
     *
     * @param args command-line arguments passed to the application (e.g. {@code job=someJob})
     * @throws Exception if any error occurs during job execution
     */
    @Override
    public void run(String... args) throws Exception {

        // 1. Parse command-line arguments into a key-value map
        Map<String, String> arguments = new HashMap<>();

        for(String arg : args) {
            String[] keyValue = arg.split("=");
            arguments.put(keyValue[0], keyValue[1]);
        }

        // 2. Create job parameters including a timestamp and the job ID
        JobParameters params = new JobParametersBuilder()
                .addLong(PARAM_TIMESTAMP, System.currentTimeMillis(), true)
                .addString(PARAM_JOB_NAME, batchJobName, true)
                .toJobParameters();

        // 3. Retrieve the Job from the registry using the job name
        for(String name : jobRegistry.getJobNames()) {
            logger.debug("Found Job Registry: {}", name);
        }
        Job job = jobRegistry.getJob(batchJobName);

        // 4. Launch the job
        logger.info("\uD83D\uDE80 Starting batch job execution");
        logger.debug("   - Job = {}", job.getName());

        JobExecution jobExecution = jobLauncher.run(job, params);

        // 5. Print job execution result
        logger.info("✅ Batch job completed with status: " + jobExecution.getStatus());
    }
}
