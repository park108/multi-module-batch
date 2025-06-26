# 개요
- 프로젝트에서 활용할 멀티 모듈 Spring Batch 프로젝트를 만든다.
- 정상적으로 동작하는지 확인한다.

# 구조
## 멀티 모듈 프로젝트
- Common 모듈과 Job 모듈로 구성된다.
- 프로젝트 수행 모드에서는 Common 모듈을 라이브러리로 제공하므로 프로젝트 구조에서 제거된다.
- Job 모듈은 Common 모듈을 의존성 설정하여 사용한다.
## 모듈 및 패키지 구조
### Project root
- settings.gradle: 모듈 등록
### nova-batch-common
- 배치에서 샤용할 공통 기능을 모아둔 모듈이다.
- com.skt.nova.batch.common 패키지에 위치한다.
- Job 모듈 패키지(com.skt.nova.batch) 하위 패키지로 설정하여 컴포넌트 스캔이 가능하게 한다.
### combat0001, combat0002
- 임시 네이밍 룰: {업무코드3}{상세업무코드3}{일련번호4}
  - TODO: 향후 네이밍 룰 확정 후 수정하여 가이드한다.
- 실제 비즈니스 애플리케이션을 구현하는 모듈이다.
- com.skt.nova.batch 패키지에 위치한다.
  - BatchApplication 클래스가 존재하며, main() 메소드를 이 클래스에 구현한다.
- 각 Job별로 필요한 의존성, 애플리케이션, 로깅 설정을 한다.
  - build.gradle: 의존성
  - application.yml: 애플리케이션 설정
  - logback-spring.xml: 로깅 설정

# 빌드
- gradle로 빌드한 프로젝트는 각 모듈 내 build 디렉토리에 생성된다.
- TODO: 스크립트 제공

# 실행, 배포
- 빌드된 jar 파일을 직접 실행하거나 containerizing 하여 이미지로 배포할 수 있다.
- TODO: 쿠버네티스 배포 절차 제공 