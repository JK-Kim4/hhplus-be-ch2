package kr.hhplus.be.server.integration.support;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.item.ItemRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestBeans {

    @Autowired public CouponRepository couponRepository;
    @Autowired public ItemRepository itemRepository;
    @Autowired public OrderRepository orderRepository;
    @Autowired public PaymentRepository paymentRepository;
    @Autowired public UserRepository userRepository;
    @Autowired public InitialTestData initialTestData;
    @Autowired public DatabaseCleanup databaseCleanup;

}
