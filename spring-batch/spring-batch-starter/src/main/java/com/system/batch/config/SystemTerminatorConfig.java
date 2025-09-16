package com.system.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class SystemTerminatorConfig {
    // ./gradlew bootRun --args='--spring.batch.job.name=processTerminatorJob terminatorId=KILL-9,java.lang.String targetCount=5,java.lang.Integer'

    @Bean
    public Job processTerminatorJob(JobRepository jobRepository, Step terminationStep) {
        return new JobBuilder("processTerminatorJob", jobRepository)
                .start(terminationStep)
                .build();
    }

    @Bean
    public Step terminationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet terminatorTasklet) {
        return new StepBuilder("terminationStep", jobRepository)
                .tasklet(terminatorTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet terminatorTasklet(
            @Value("#{jobParameters['terminatorId']}") String terminatorId, // 문자열(String) 타입 파라미터
            @Value("#{jobParameters['targetCount']}") Integer targetCount // 정수(Integer) 타입 파라미터

    ) {
        return (contribution, chunkContext) -> {
            log.info("시스템 종결자 정보");
            log.info("Id: {}", terminatorId);
            log.info("제거 대상 수: {}", targetCount);
            log.info("☠️ {}개의 프로세스를 종료합니다.", targetCount);

            for (int i = 0; i < targetCount; i++) {
                log.info("💀 프로세스 {} 종료 완료", i);
            }
            log.info("모든 대상 프로세스 종료 완료");
            return RepeatStatus.FINISHED;
        };
    }
}
