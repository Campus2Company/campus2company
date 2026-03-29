package com.campus2company.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"message.sent", "account.status.changed"})
class NotificationServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
