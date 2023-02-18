package book.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerMain {
    public static final Logger log = LoggerFactory.getLogger(ConsumerMain.class);

    public static void main(String[] args) {
        KafkaConsumer<String,String> consumer = buildConsumer();

        // set up exit hook
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("in shutdown hook..");
            consumer.wakeup();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
            }
        }));

        Duration timeout = Duration.ofMillis(100);
        boolean keepListening = true;

        try {
            while (keepListening) {
                ConsumerRecords<String, String> records = consumer.poll(timeout);
                for (ConsumerRecord<String, String> rec : records) {
                    new BasicRecordProcessor().handle(rec);
                    if (rec.value().contains("exit")){
                        keepListening = false;
                    }
                }
            }
        } catch (Exception e) {
            log.info("woke from poll; shutting down");
            consumer.close();
        }
    }


    private static KafkaConsumer<String, String> buildConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "testing");
        props.put("client.id",ConsumerMain.class.getCanonicalName());

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("test"));
        return consumer;
    }
}
