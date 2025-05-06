package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Price;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void pay_정상적으로_결제된다() {
        // given
        Long userId = 1L;
        Long orderId = 10L;
        Long productId = 100L;
        int quantity = 2;
        BigDecimal price = BigDecimal.valueOf(1000);
        OrderItem orderItem = OrderItem.create(productId, Price.of(price), quantity);
        List<OrderItem> orderItemList = List.of(orderItem);

        Order order = Order.create(userId, orderItemList);
        Balance balance = mock(Balance.class);
        Product product = mock(Product.class);


        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderRepository.findOrderItemsByOrderId(orderId)).willReturn(orderItemList);
        given(productRepository.findByIdInWithPessimisticLock(List.of(productId)))
                .willReturn(List.of(product));
        given(balanceRepository.findByUserId(userId)).willReturn(Optional.of(balance));

        // when
        PaymentCommand.Pay command = PaymentCommand.Pay.of(userId, orderId);
        PaymentInfo.Pay result = paymentService.pay(command);

        // then
        assertThat(result).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(orderRepository).findById(orderId);
        verify(productRepository).findByIdInWithPessimisticLock(List.of(productId));
        verify(balanceRepository).findByUserId(userId);
        verify(paymentRepository).save(any(Payment.class));
    }
}
