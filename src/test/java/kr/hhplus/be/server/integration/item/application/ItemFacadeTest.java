package kr.hhplus.be.server.integration.item.application;

import kr.hhplus.be.server.application.item.ItemFacade;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.rank.Rank;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.support.DatabaseCleanup;
import kr.hhplus.be.server.integration.support.InitialTestData;
import kr.hhplus.be.server.integration.support.SampleValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ItemFacadeTest {

    @Autowired
    ItemFacade itemFacade;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InitialTestData initialTestData;

    @Autowired
    DatabaseCleanup databaseCleanup;

    SampleValues sampleValues;

    User user;
    Order order;

    @BeforeEach
    void setUp() {
        databaseCleanup.truncate();
        sampleValues = initialTestData.load();

        user = sampleValues.user;
        userRepository.save(user);
        order = sampleValues.createSampleOrderWithUser(user);
        orderRepository.save(order);
    }

    @Test
    @Transactional
    void 집계_일시를_전달받아_결제완료된_주문의_상품_갯수만큼_순위데이터가_생성된다(){
        //given
        LocalDate targetDate = LocalDate.of(2024,12,31);
        order.updateOrderDate(targetDate);
        order.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        //when
        List<Rank> rankList = itemFacade.createRankList(targetDate);

        //then
        assertEquals(order.getOrderItems().size(), rankList.size());
    }
}
