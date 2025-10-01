package com.system.batch.jobparam;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class TerminatorConfig {
    // ./gradlew bootRun --args='--spring.batch.job.name=terminatorJob executionDate=2025-10-01,java.time.LocalDate startTime=2025-10-01T14:30:00,java.time.LocalDateTime'

    @Bean
    public Job terminatorJob(JobRepository jobRepository, Step terminationStep2) {
        return new JobBuilder("terminatorJob", jobRepository)
                .start(terminationStep2)
                .build();
    }

    @Bean
    public Step terminationStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet terminatorTasklet2) {
        return new StepBuilder("terminationStep2", jobRepository)
                .tasklet(terminatorTasklet2, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet terminatorTasklet2(
            @Value("#{jobParameters['executionDate']}")LocalDate executionDate,
            @Value("#{jobParameters['startTime']}")LocalDateTime startTime
    ) {
        return (contribution, chunkContext) -> {
            log.info("시스템 처형 정보: ");
            log.info("처형 예정일: {}", executionDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")));
            log.info("시작 시각: {}", startTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")));
            log.info("{}에 예정된 시스템 정리를 시작합니다.", executionDate);
            log.info("시작 시각: {}", startTime);

            LocalDateTime currentTime = startTime;
            for (int i = 1; i <= 3; i++) {
                currentTime = currentTime.plusHours(1);
                log.info("시스템 정리 {} 시간 경과... 현재 시각: {}", i, currentTime.format(DateTimeFormatter.ofPattern("HH시 mm분")));
            }

            log.info("임무 완료: 모든 대상 시스템이 성공적으로 제거되었습니다.");
            log.info("작전 종료 시각: {}", currentTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")));

            return RepeatStatus.FINISHED;
        };
    }
}
