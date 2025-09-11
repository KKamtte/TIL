# Spring Boot Batch 프로젝트 구성

## 실행
```bash
./gradlew bootRun --args='--spring.batch.job.name=systemTerminationSimulationJob'
```

## 결과
```

> Task :bootRun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.7)

2025-09-11T12:33:38.580+09:00  INFO 64688 --- [kill-batch-system] [           main] c.s.batch.KillBatchSystemApplication     : Starting KillBatchSystemApplication using Java 17.0.16 with PID 64688 (/Users/seunghyeon.kim/study/TIL/spring-batch/spring-batch-starter/build/classes/java/main started by seunghyeon.kim in /Users/seunghyeon.kim/study/TIL/spring-batch/spring-batch-starter)
2025-09-11T12:33:38.581+09:00  INFO 64688 --- [kill-batch-system] [           main] c.s.batch.KillBatchSystemApplication     : No active profile set, falling back to 1 default profile: "default"
2025-09-11T12:33:38.795+09:00  INFO 64688 --- [kill-batch-system] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-09-11T12:33:38.860+09:00  INFO 64688 --- [kill-batch-system] [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:da74c5b4-1b61-4908-8518-3f8cba9c45a6 user=SA
2025-09-11T12:33:38.861+09:00  INFO 64688 --- [kill-batch-system] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2025-09-11T12:33:38.948+09:00  INFO 64688 --- [kill-batch-system] [           main] c.s.batch.KillBatchSystemApplication     : Started KillBatchSystemApplication in 0.496 seconds (process running for 0.606)
2025-09-11T12:33:38.949+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2025-09-11T12:33:38.965+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=systemTerminationSimulationJob]] launched with the following parameters: [{}]
2025-09-11T12:33:38.973+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [enterWorldStep]
System Termination 시작
2025-09-11T12:33:38.977+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.step.AbstractStep         : Step: [enterWorldStep] executed in 3ms
2025-09-11T12:33:38.979+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [meetNPCStep]
시스템 관리자 NPC 만났습니다.
첫번째 좀비 프로세스 5개 처리하기
2025-09-11T12:33:38.980+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.step.AbstractStep         : Step: [meetNPCStep] executed in 1ms
2025-09-11T12:33:38.982+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [defeatProcessStep]
좀비 프로세스 제거 완료 (현재 1/5)
좀비 프로세스 제거 완료 (현재 2/5)
좀비 프로세스 제거 완료 (현재 3/5)
좀비 프로세스 제거 완료 (현재 4/5)
좀비 프로세스 제거 완료 (현재 5/5)
2025-09-11T12:33:38.986+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.step.AbstractStep         : Step: [defeatProcessStep] executed in 4ms
2025-09-11T12:33:38.987+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [completeQuestStep]
미션 완료. 좀비 프로세스 5 개 제거
2025-09-11T12:33:38.988+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.batch.core.step.AbstractStep         : Step: [completeQuestStep] executed in 1ms
2025-09-11T12:33:38.991+09:00  INFO 64688 --- [kill-batch-system] [           main] o.s.b.c.l.s.TaskExecutorJobLauncher      : Job: [SimpleJob: [name=systemTerminationSimulationJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 21ms
2025-09-11T12:33:38.993+09:00  INFO 64688 --- [kill-batch-system] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2025-09-11T12:33:38.996+09:00  INFO 64688 --- [kill-batch-system] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

BUILD SUCCESSFUL in 1s
4 actionable tasks: 3 executed, 1 up-to-date
```
