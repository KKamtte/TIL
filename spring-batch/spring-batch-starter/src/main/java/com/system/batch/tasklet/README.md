# 태스크릿 처리 (TaskletStep)

## 개념
Spring Batch에서 가장 기본적인 Step 구현 방식으로 비교적 복잡하지 않은 단순한 작업을 실행할 때 사용

SpringBatch 의 Step은 읽기-처리-쓰기 의 ETL 작업에 초점을 맞춘다.

단순한 시스템 작업이나 유틸성 작업이 필요한 단일 비즈니스 로직 실행에 초점을 맞출 경우 TaskletStep 로 설계한다.
- 매일 새벽 불필요한 로그 파일 삭제
- 득정 디렉토리에서 오래된 파일 아카이브
- 사용자에게 단순한 알림 메시지 또는 이메일 발송
- 외부 API 호출 후 결과를 단순히 저장하거나 로깅

## 예시
```
@Slf4j
public class ZombieProcessCleanupTasklet implements Tasklet {
    private final int processesToKill = 10;
    private int killedProcesses = 0;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        killedProcesses++;
        log.info("☠️  프로세스 강제 종료... ({}/{})", killedProcesses, processesToKill);

        if (killedProcesses >= processesToKill) {
            log.info("💀 시스템 안정화 완료. 모든 좀비 프로세스 제거.");
            return RepeatStatus.FINISHED;  // 모든 프로세스 종료 후 작업 완료
        }

        return RepeatStatus.CONTINUABLE;  // 아직 더 종료할 프로세스가 남아있음
    }
}
```
좀비 프로세스를 반복적으로 처리하는 작업을 Tasklet 으로 구현한 예제

`execute()` 메서드 내에 프로세스를 하나씩 종료하며 목표치를 달성하면 종료

## RepeatStatus
- `RepeatStatus.FINISHED`: Step의 처리가 성공/실ㅍ와 관계없이 Step 이 완료됨. 더 이상 반복 없이 다음 단계로 넘어감
- `RepeatStatus.CONTINUABLE`: Tasklet 에서 실행 중인 메서드가 추가로 더 실행되어야 함을 알리는 신호, Step 종료는 보류되고 필요한 만큼 메서드가 반복됨

## while vs RepeatStatus
오래된 주문 데이터를 정리하는 배치 작업에서 한 번에 만 건식 데이터를 삭제하고, 총 100만 건의 데이터를 처리한다고 가정
- `while`: 80만 건째 처리 중 예외 발생 시, 이미 처리한 79만 건의 데이터도 모두 롤백되어 하나도 정리되지 않은 상태로 돌아감
- `RepeatStatus.CONTINUABLE`: 매 만 건 처리마다 트랜잭션이 커밋되므로, 예외가 발생하더라도 79만 건의 데이터는 정리됨
