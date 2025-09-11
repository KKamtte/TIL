package com.system.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 매일 밤 7일이 지난 레코드를 created 컬럼 기준으로 삭제하는 작업
 * 단순한 작업이라면 별도의 Tasklet 구현 클래스를 만들지 않고, 람다 표현식을 사용해 Step 구성 중에 바로 Tasklet을 정의
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DeleteRecordConfig {

    private final JobRepository jobRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step deleteOldRecordsStep() {
        return new StepBuilder("deleteOldRecordsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int deleted = jdbcTemplate.update("DELETE FROM logs WHERE created < NOW() - INTERVAL '7 DAY'");
                    log.info("{} 개의 오래된 레코드가 삭제되었습니다. ", deleted);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job deleteOldRecordsJob() {
        return new JobBuilder("deleteOldRecordsStep", jobRepository)
                .start(deleteOldRecordsStep())
                .build();
    }
}
