package com.prgrms.zzalmyu.core.config;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ChatMessage;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ChatMessageRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.util.StringUtils;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BatchProperties.class)
public class BatchConfig {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
        JobRepository jobRepository, BatchProperties properties) {
        JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobNames = properties.getJob().getName();
        if (StringUtils.hasText(jobNames)) {
            runner.setJobName(jobNames);
        }
        return runner;
    }

    @Bean
    public Job deleteJob() {
        return new JobBuilder("deleteChatMessageJob", jobRepository)
                .start(deleteChatMessageStep())
                .next(deleteUserStep())
                .build();
    }

    @Bean
    @JobScope
    public Step deleteChatMessageStep() {
        return new StepBuilder("deleteChatMessageStep", jobRepository)
                .<ChatMessage, ChatMessage>chunk(1000, transactionManager)
                .reader(chatMessageReader())
                .writer(chatMessageWriter())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ChatMessage> chatMessageReader() {
        LocalDate today = LocalDate.now();
        LocalDateTime twoDaysAgo = LocalDateTime.of(today.minusDays(2), LocalTime.MIDNIGHT);
        Map<String, Direction> sortProperties = new HashMap<>();
        sortProperties.put("id", Direction.ASC);
        return new RepositoryItemReaderBuilder<ChatMessage>()
                .repository(chatMessageRepository)
                .methodName("findBeforeTwoDays")
            .arguments(twoDaysAgo)
                .name("chatMessageReader")
                .pageSize(1000)
            .sorts(sortProperties)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<ChatMessage> chatMessageWriter() {
        return new RepositoryItemWriterBuilder<ChatMessage>()
                .repository(chatMessageRepository)
                .methodName("delete")
                .build();
    }

    @Bean
    @JobScope
    public Step deleteUserStep() {
        return new StepBuilder("deleteUserStep", jobRepository)
            .<User, User>chunk(1000, transactionManager)
            .reader(userReader())
            .writer(userWriter())
            .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<User> userReader() {
        LocalDate today = LocalDate.now();
        LocalDateTime oneWeekAgo = LocalDateTime.of(today.minusDays(7), LocalTime.MIDNIGHT);
        Map<String, Direction> sortProperties = new HashMap<>();
        sortProperties.put("id", Direction.ASC);
        return new RepositoryItemReaderBuilder<User>()
            .repository(userRepository)
            .methodName("findDeletedOneWeekAgo")
            .arguments(oneWeekAgo)
            .name("userReader")
            .pageSize(1000)
            .sorts(sortProperties)
            .build();
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<User> userWriter() {
        return new RepositoryItemWriterBuilder<User>()
            .repository(userRepository)
            .methodName("delete")
            .build();
    }
}