package kr.hhplus.be.server.domain.coupon;

import jakarta.annotation.PostConstruct;
import kr.hhplus.be.server.infrastructure.coupon.InMemoryCouponIssueQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueWorker {

    private final InMemoryCouponIssueQueue couponIssueQueue;
    private final CouponService couponService;

    @PostConstruct
    public void startWorker() {
        new Thread(() -> {
            System.out.println("[Worker Started]");

            while (true) {
                try {
                    InMemoryCouponIssueQueue.Command command = couponIssueQueue.dequeue();
                    System.out.println("dequeue uuid:  " + command.getUuid() + " couponId: " + command.getCouponId());
                    couponService.deductCouponQuantity(command.getCouponId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.out.println("[Error] " + e.getMessage());
                }
            }
        }).start();
    }
}
