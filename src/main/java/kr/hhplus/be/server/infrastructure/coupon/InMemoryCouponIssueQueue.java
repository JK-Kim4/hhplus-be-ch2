package kr.hhplus.be.server.infrastructure.coupon;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class InMemoryCouponIssueQueue {

    private final BlockingQueue<Command> queue = new LinkedBlockingQueue<>();

    public void enqueue(Long couponId, String uuid) {
        System.out.println("enqueue uuid:  " + uuid + " couponId: " + couponId);
        queue.add(new Command(couponId, uuid));
    }

    public Command dequeue() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }

    @Getter
    public static class Command {
        Long couponId;
        String uuid;

        public Command(Long couponId, String uuid) {
            this.couponId = couponId;
            this.uuid = uuid;
        }
    }
}
