package com.skt.nova.batch;

import com.skt.nova.batch.common.NovaBatchUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        NovaBatchUtil.validateBatchStartup(args);
        SpringApplication.run(BatchApplication.class, args);
    }
}