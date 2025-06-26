# combat0002 
- Job 모듈
- 단순 Chunk 처리 예시
## 구조
```
combat0002
├── BatchApplication.java
├── config
│   └── BatchConfig.java
├── entity
│   ├── Invoice.java
│   ├── InvoiceRepository.java
│   └── InvoiceRowMapper.java
└── step001
    ├── InvoiceProcessor.java
    └── InvoiceWriter.java
```
### BatchApplication.java
- Spring Boot 애플리케이션 실행을 위한 main() 메소드 위치
### BatchConfig.java
- Job, Step 정의
### entity package
- JPA 사용을 위한 코드 위치
### step001 pacakge
- Step 구현체 위치
- 각 step의 ItemReader, ItemProcessor, ItemWriter 커스텀 구현