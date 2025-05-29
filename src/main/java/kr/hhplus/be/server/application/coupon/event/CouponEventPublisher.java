package kr.hhplus.be.server.application.coupon.event;

import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;

public interface CouponEventPublisher {

    void send(CouponIssueRequestedEvent event);

    void fail(CouponIssueRequestedEvent event);

}
