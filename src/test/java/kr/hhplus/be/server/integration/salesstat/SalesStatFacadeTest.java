package kr.hhplus.be.server.integration.salesstat;

import kr.hhplus.be.server.application.salesstat.SalesStatFacade;
import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReport;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.support.domainsupport.OrderDomainSupporter;
import kr.hhplus.be.server.support.domainsupport.UserDomainSupporter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Testcontainers
public class SalesStatFacadeTest {


    @Autowired
    SalesStatFacade salesStatFacade;

    @Autowired
    RedissonClient redissonClient;

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
        LocalDate targetDate = LocalDate.of(2025, 1, 1);
        String key = CacheKeys.DAILY_SALES_REPORT.format(targetDate);
        salesStatFacade.dailySalesReportProcess(order.getId(), targetDate);

        //then
        RScoredSortedSet<Long> scoredSortedSet = redissonClient.getScoredSortedSet(key);
        List<SalesReport> list = scoredSortedSet.entryRange(0, -1).stream().map(
                entry -> SalesReport.of(entry.getValue(), entry.getScore(), targetDate)
        ).toList();


        //then
        Assertions.assertThat(list.stream().map(SalesReport::getProductId).toList())
                .contains(1L, 2L, 3L);
    }

}
