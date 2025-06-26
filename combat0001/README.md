# combat0001 
- Job 모듈
- 단순 Taslket 처리 예시
## 구조
```
combat0001
├── BatchApplication.java
├── config
│   └── BatchConfig.java
└── step001
    └── SimpleTasklet.java
```
### BatchApplication.java
- Spring Boot 애플리케이션 실행을 위한 main() 메소드 위치
### BatchConfig.java
- Job, Step 정의
### SimpleTasklet.java
- Tasklet 인터페이스의 구현체
- execute() 메소드에 실행 로직 구현
- 배치 자체 로직, 또는 온라인 모듈 임포트하여 실행