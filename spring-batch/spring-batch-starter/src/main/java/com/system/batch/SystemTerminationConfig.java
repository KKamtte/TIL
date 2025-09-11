package com.system.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class SystemTerminationConfig {

    private AtomicInteger processeskilled = new AtomicInteger(0);
    private final int TERMINATION_TARGET = 5;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public SystemTerminationConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job systemTerminationSimulationJob() {
        return new JobBuilder("systemTerminationSimulationJob", jobRepository)
                .start(enterWorldStep())
                .next(meetNCPStep())
                .next(defeatProcessStep())
                .next(completeQuestStep())
                .build();
    }

    private Step enterWorldStep() {
        return new StepBuilder("enterWorldStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("System Termination 시작");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    private Step meetNCPStep() {
        return new StepBuilder("meetNPCStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("시스템 관리자 NPC 만났습니다.");
                    System.out.println("첫번째 좀비 프로세스 " + TERMINATION_TARGET + "개 처리하기");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    private Step defeatProcessStep() {
        return new StepBuilder("defeatProcessStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int terminated = processeskilled.incrementAndGet();
                    System.out.println("좀비 프로세스 제거 완료 (현재 " +terminated+ "/" +TERMINATION_TARGET+ ")");
                    if (terminated < TERMINATION_TARGET) {
                        return RepeatStatus.CONTINUABLE;
                    } else {
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    private Step completeQuestStep() {
        return new StepBuilder("completeQuestStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("미션 완료. 좀비 프로세스 " + TERMINATION_TARGET + " 개 제거");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
