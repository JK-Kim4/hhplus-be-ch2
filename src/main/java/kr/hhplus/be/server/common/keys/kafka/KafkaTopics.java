package kr.hhplus.be.server.common.keys.kafka;

public class KafkaTopics {

    protected KafkaTopics(){
        throw new IllegalStateException("External topics class cannot be instantiated");
    }

    public static final String PAYMENT_COMPLETED_TOPIC = "payment.completed";
    public static final String PAYMENT_COMPLETED_DLQ = "payment.completed.dlq";

}
