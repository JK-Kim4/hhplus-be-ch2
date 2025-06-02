package kr.hhplus.be.server.common.keys.kafka;

public class KafkaConsumerGroups {

    protected KafkaConsumerGroups(){
        throw new IllegalStateException("External topics class cannot be instantiated");
    }

    public static final String PAYMENT_COMPLETE_GROUP = "payment-complete-group";
    public static final String COUPON_ISSUE_REQUESTED_GROUP = "coupon-issue-requested-group";
}
