# Common batch module: nova-batch-common
## Version 0.0.1
### com.skt.nova.batch.common
- NovaBatchProperties
- NovaBatchUtil
#### executor
- NovaBatchJobRunner
    - CommandLineRunner 기반의 컴포넌트로, Spring Boot 기반 서비스가 기동할 때 오버라이드한 run() 메소드가 자동 실행된다.
    - Java argument로 들어온 값들을 JobParameter로 구성해준다.
- NovaTaskExecutorConfig
#### listener
- NovaJobExecutionListener
- NovaStepExecutionListener
#### item
- NovaItemReader
- NovaItemReaderBuilder