package com.campus2company.notification.config;

import com.campus2company.common.event.AccountStatusChangedEvent;
import com.campus2company.common.event.MessageSentEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.campus2company.common.event");
        return props;
    }

    @Bean
    public ConsumerFactory<String, MessageSentEvent> messageSentConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseConsumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(MessageSentEvent.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageSentEvent> messageSentListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageSentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageSentConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AccountStatusChangedEvent> accountStatusConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseConsumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(AccountStatusChangedEvent.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountStatusChangedEvent> accountStatusListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountStatusChangedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountStatusConsumerFactory());
        return factory;
    }
}
