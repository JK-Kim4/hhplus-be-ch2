package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentService paymentService;

    User user;
    Item item;
    Order order;
    OrderItem orderItem;
    Payment payment;
    List<OrderItem> orderItems = new ArrayList<>();

    @BeforeEach
    void setUp() {
        user = new User(1L, "Test");
        order = new Order(user);
        item = new Item("test", 5000, 50);
        orderItem = new OrderItem(order, item, 10);
        orderItems.add(orderItem);
        order.calculateTotalPrice(orderItems);
        payment = new Payment(order);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
    }

    @AfterEach
    void tearDown() {
        orderItems = new ArrayList<>();
        Mockito.reset(orderRepository, paymentRepository);
    }

    @Test
    @DisplayName("주문 고유번호를 전달(command)받아 결제를 생성하고 이력을 저장한다.")
    void payment_save_test(){
        //given
        PaymentCreateCommand command = new PaymentCreateCommand(order.getId());

        //when
        PaymentCreateCommand.Response result = paymentService.save(command);

        //then
        assertAll(
            "결제 대기 상태의 결제를 생성하고 이력을 저장한다.",
            () -> assertEquals(PaymentStatus.PAYMENT_PENDING, result.getPayment().getPaymentStatus()),
            () -> verify(paymentRepository, times(1)).savePaymentHistory(any())
        );
    }

    @Test
    @DisplayName("결제처리가 완료되면 사용자의 잔액이 차감되고 결제 이력이 저장된다.")
    void payment_process_test(){
        //given
        user.chargePoint(50000);
        PaymentProcessCommand paymentProcessCommand = new PaymentProcessCommand(payment.getId());

        //when
        PaymentProcessCommand.Response response = paymentService.processPayment(paymentProcessCommand);
        payment = response.getPayment();

        //then
        assertEquals(PaymentStatus.PAYMENT_COMPLETED, payment.getPaymentStatus());
        verify(paymentRepository, times(1)).savePaymentHistory(any());

    }

    @Test
    @DisplayName("결제 요청 주문이 존재하지 않을 경우 오류를 반환한다.")
    void payment_process_order_not_found_test() {
        //given
        PaymentCreateCommand command = new PaymentCreateCommand(99L);
        when(orderRepository.findById(command.getOrderId())).thenReturn(Optional.empty());

        //then
        assertThrows(NoResultException.class, () -> paymentService.save(command));
    }

    @Test
    @DisplayName("결제 진행시 사용자 잔액이 부족할 경우 오류를 반환한다.")
    void payment_process_insufficient_point_test(){
        //given
        user.chargePoint(100);
        PaymentProcessCommand paymentProcessCommand = new PaymentProcessCommand(payment.getId());

        //when
        InvalidAmountException invalidAmountException = assertThrows(InvalidAmountException.class,
                () -> paymentService.processPayment(paymentProcessCommand));

        //then
        assertEquals(InvalidAmountException.INSUFFICIENT_BALANCE, invalidAmountException.getMessage());
    }

    @Test
    @DisplayName("고유번호에 해당하는 결제가 존재할 경우 결제를 반환한다.")
    void payment_find_by_id_test() {
        // given
        Payment mockPayment = mock(Payment.class);
        when(paymentRepository.findById(77L)).thenReturn(Optional.of(mockPayment));

        // when
        Payment result = paymentService.findById(77L);

        // then
        assertEquals(mockPayment, result);
    }

    @Test
    @DisplayName("고유번호에 해당하는 결제가 존재하지 않을 경우 오류를 반환한다.")
    void payment_find_by_id_not_found_test() {
        //given
        when(paymentRepository.findById(77L)).thenReturn(Optional.empty());

        //then
        assertThrows(NoResultException.class,
                () -> paymentService.findById(77L));
    }

}
