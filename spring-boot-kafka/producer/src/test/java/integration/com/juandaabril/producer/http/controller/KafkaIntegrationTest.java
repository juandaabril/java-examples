package com.juandaabril.producer.http.controller;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"books"}, partitions = 3)
@TestPropertySource(properties = {
    "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
@DirtiesContext
public class KafkaIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected EmbeddedKafkaBroker embeddedKafkaBroker;

    protected Consumer<Integer, String> consumer;

    @BeforeEach
    public void setUp() {
        Map<String, Object> configs = new HashMap<String, Object>(
            KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker)
        );
        consumer = new DefaultKafkaConsumerFactory<Integer, String>(
            configs,
            new IntegerDeserializer(),
            new StringDeserializer()
        ).createConsumer();

        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

}
