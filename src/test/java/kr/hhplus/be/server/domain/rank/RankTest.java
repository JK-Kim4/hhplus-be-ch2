package kr.hhplus.be.server.domain.rank;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderTestFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RankTest {

    @Nested
    class 판매상품_목록을_전달받아_판매순위_목록을_생성{

        @Test
        void 대상이되는_판매상품의_주문날짜는_모두_동일해야한다(){
            //given
            Order order = OrderTestFixture.createTestOrder();






        }

    }
}
