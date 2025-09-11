# 순수 Spring Batch 프로젝트 구성

## 실행
```bash
./gradlew run --args="com.system.batch.config.SystemTerminationConfig systemTerminationSimulationJob"
```

## 결과
```
Reusing configuration cache.

> Task :app:run
SLF4J: No SLF4J providers were found.
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See https://www.slf4j.org/codes.html#noProviders for further details.
System Termination 시작
시스템 관리자 NPC 만났습니다.
첫번째 좀비 프로세스 5개 처리하기
좀비 프로세스 제거 완료 (현재 1/5)
좀비 프로세스 제거 완료 (현재 2/5)
좀비 프로세스 제거 완료 (현재 3/5)
좀비 프로세스 제거 완료 (현재 4/5)
좀비 프로세스 제거 완료 (현재 5/5)
미션 완료. 좀비 프로세스 5 개 제거

BUILD SUCCESSFUL in 936ms
2 actionable tasks: 2 executed
Configuration cache entry reused.
```
