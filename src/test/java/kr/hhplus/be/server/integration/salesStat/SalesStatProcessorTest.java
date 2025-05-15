package kr.hhplus.be.server.integration.salesStat;

import kr.hhplus.be.server.application.salesStat.SalesStatProcessor;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.redis.RedisCommonStore;
import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.support.domainSupport.OrderDomainSupporter;
import kr.hhplus.be.server.support.domainSupport.UserDomainSupporter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Testcontainers
public class SalesStatProcessorTest {

    @Autowired
    SalesStatProcessor salesStatProcessor;

    @Autowired
    RedisZSetStore redisZSetStore;

    @Autowired
    RedisCommonStore redisCommonStore;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    OrderRepository orderRepository;

    Order order;

    @BeforeEach
    void setup(){
        User user = UserDomainSupporter.기본_사용자_생성();
        userRepository.save(user);
        Balance balance = UserDomainSupporter.사용자_잔고_생성(user.getId(), BigDecimal.valueOf(100_000));
        balanceRepository.save(balance);
        order = OrderDomainSupporter.사용자_고유번호를_전달받아_기본_주문_생성(user.getId());
        orderRepository.save(order);


        userRepository.flush();
        balanceRepository.flush();
        orderRepository.flush();
    }

    @Test
    void 결제_완료된_주문의_고유번호를_전달받아_판매_상품_정보를_실시간_판매_집계_KEY에_추가한다(){
        //given
        String key = SalesStatProcessor.getDailySalesReportKey(LocalDate.now());
        salesStatProcessor.dailySalesReportProcess(order.getId(), key);

        //then
        List<TypedScore> zSetRangeWithScoresByKey = redisZSetStore.rangeWithScores(key);

        Assertions.assertThat(zSetRangeWithScoresByKey.stream().map(TypedScore::member).toList())
                .contains("1", "2", "3");
    }

}
