package com.juandaabril.producer.http.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic booksTopic() {
        return TopicBuilder
            .name("books")
            .partitions(3)
            .replicas(1)
            .build();
    }
}
