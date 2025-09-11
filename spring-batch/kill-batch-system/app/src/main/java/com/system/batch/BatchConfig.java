package com.system.batch;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// DefaultBatchConfiguration: JobRepository, JobLauncher 등 Spring Batch의 핵심 컴포넌트들을 자동으로 구성
@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    // Job 과 Step 의 실행정보를 저장하는 Datasource 설정
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    // 저장, 실행 등의 작업은 트랜잭션 내에서 처리되도록함
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
