package com.prgrms.zzalmyu.core.config;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ChatMessage;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final ChatMessageRepository chatMessageRepository;

    @Bean
    public Job deleteChatMessageJob(JobRepository jobRepository, Step deleteChatMessageStep) {
        return new JobBuilder("deleteChatMessageJob", jobRepository)
                .start(deleteChatMessageStep)
                .build();
    }

    @Bean
    @JobScope
    public Step deleteChatMessageStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteChatMessageStep", jobRepository)
                .<ChatMessage, ChatMessage>chunk(1000, transactionManager)
                .reader(chatMessageReader())
                .writer(chatMessageWriter())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ChatMessage> chatMessageReader() {
        return new RepositoryItemReaderBuilder<ChatMessage>()
                .repository(chatMessageRepository)
                .methodName("findBeforeTwoDays")
                .name("chatMessageReader")
                .pageSize(1000)
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
}