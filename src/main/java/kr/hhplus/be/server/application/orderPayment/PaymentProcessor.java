package kr.hhplus.be.server.application.orderPayment;


@FunctionalInterface
public interface PaymentProcessor<T, R> {
    R process(T inputCriteria);

    default <V> PaymentProcessor<T, V> andThen(PaymentProcessor<? super R, ? extends V> after) {
        return input -> after.process(this.process(input));
    }

}
