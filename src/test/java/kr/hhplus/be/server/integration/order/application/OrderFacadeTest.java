package kr.hhplus.be.server.integration.order.application;

import kr.hhplus.be.server.application.orderPayment.OrderFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class OrderFacadeTest {

    @Autowired
    OrderFacade orderFacade;

    @Test
    void 주문을_생성한다(){

    }
}
