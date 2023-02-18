package book.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;

public class ProducerTest {
    private static Logger log = LoggerFactory.getLogger(ProducerTest.class);

//    @Test
    void testProduce(){
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("retries","3");
        props.put("client.id","ProducerTest java code");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = new ProducerRecord<>("test", "test message","stuck on bandaids");
        try {
            RecordMetadata meta = producer.send(record).get();
            log.info("result.offset=> "+meta.offset());
        } catch (Exception e) {
            fail(e);
            throw new RuntimeException(e);
        } finally {
            producer.close();
        }

    }
}
