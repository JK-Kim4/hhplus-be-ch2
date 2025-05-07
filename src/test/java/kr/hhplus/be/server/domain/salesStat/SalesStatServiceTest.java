package kr.hhplus.be.server.domain.salesStat;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.product.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalesStatServiceTest {

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    SalesStatService salesStatService;


    @Test
    void 판매_통계를_정상적으로_조회한다() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 7, 12, 0);
        LocalDate date = dateTime.toLocalDate();
        SalesStatCommand.SalesStats command = SalesStatCommand.SalesStats.of(dateTime.toLocalDate());

        List<Payment> payments = List.of(
                Payment.create(1L, 1L, Price.of(BigDecimal.valueOf(10_000)), dateTime),
                Payment.create(2L, 2L, Price.of(BigDecimal.valueOf(10_000)), dateTime)
        );

        when(paymentRepository.findAllByPaidDate(date)).thenReturn(payments);

        Set<Long> orderIds = Set.of(1L, 2L);
        List<OrderItem> orderItems = List.of(
                OrderItem.create(101L, Price.of(BigDecimal.valueOf(1000)), 10),
                OrderItem.create(101L, Price.of(BigDecimal.valueOf(1000)), 10),
                OrderItem.create(102L, Price.of(BigDecimal.valueOf(1000)), 10),
                OrderItem.create(103L, Price.of(BigDecimal.valueOf(1000)), 10)
        );

        when(orderRepository.findOrderItemsByOrderIds(orderIds)).thenReturn(orderItems);


        // when
        SalesStatInfo.SalesStats result = salesStatService.getSalesStatsByDate(command);

        // then
        assertAll("판매 통계 조회 테스트",
                () -> assertThat(result.getSalesStats()).hasSize(3),
                () -> assertThat(result.getSalesStats()).anySatisfy(stat ->
                        assertThat(stat.getProductId()).isEqualTo(101L)),
                () -> assertThat(result.getSalesStats()).anySatisfy(stat ->
                assertThat(stat.getProductId()).isEqualTo(102L)),
                () -> assertThat(result.getSalesAmountByProductId(101L)).isEqualTo(20)
        );
    }


}
