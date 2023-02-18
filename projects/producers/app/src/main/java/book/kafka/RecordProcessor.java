package book.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface RecordProcessor<K, V> {
    void handle(ConsumerRecord<K, V> record);
}

class BasicRecordProcessor implements RecordProcessor<String, String> {
    private static Logger log = LoggerFactory.getLogger(BasicRecordProcessor.class);

    @Override
    public void handle(ConsumerRecord<String, String> rec) {
        log.info("part({}), offset({}), value({})",
                rec.partition(),
                rec.offset(),
                rec.value());
    }
}
