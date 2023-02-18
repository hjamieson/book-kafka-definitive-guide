package book.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class ConsumerTest {
    private static Logger log = LoggerFactory.getLogger(ConsumerTest.class);

//    @Test
    void testConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "testing");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("test"));

        Duration timeout = Duration.ofMillis(100);
        boolean keepListening = true;

        while (keepListening) {
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            StreamSupport.stream(records.spliterator(), false)
                    .forEach(rec -> {
                        log.info("part({}), offset({}), value({})",
                                rec.partition(),
                                rec.offset(),
                                rec.value());
                    });
        }

    }

//    @Test
    void testConsumer2() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "testing");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("test"));

        Duration timeout = Duration.ofMillis(100);
        boolean keepListening = true;

        while (keepListening) {
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            for (ConsumerRecord<String, String> rec : records) {
                new BasicRecordProcessor().handle(rec);
                if (rec.value().contains("exit")){
                    keepListening = false;
                }
            }
        }
        log.info("we saw exit!");
        consumer.close();
    }
}
