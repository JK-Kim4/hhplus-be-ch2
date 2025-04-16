package kr.hhplus.be.server.orderPayment;

import kr.hhplus.be.server.application.orderPayment.OrderFacade;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderItemCriteria;
import kr.hhplus.be.server.application.orderPayment.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.application.orderPayment.result.OrderResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponTemplate;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.FlatDiscountCoupon;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponService;
import kr.hhplus.be.server.interfaces.adaptor.RestTemplateAdaptor;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderFacadeTest {

    @Mock
    private OrderService orderService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ItemService itemService;
    @Mock
    private UserCouponService userCouponService;
    @Mock
    private UserService userService;
    @Mock
    private RestTemplateAdaptor restTemplateAdaptor;

    @InjectMocks
    private OrderFacade orderFacade;

    Item available;
    User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L,"tester");
        available = new Item(1L, "available", 4000, 50);

        when(userService.findById(1L)).thenReturn(user);
        when(itemService.findItemById(1L)).thenReturn(available);
    }

    @Test
    void 주문을_생성한다(){
        //given
        OrderItemCriteria itemCriteria = OrderItemCriteria.of(available.getId(), 4000, 10);
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(1L, null, List.of(itemCriteria));

        //when
        OrderResult.Create orderResult = orderFacade.createOrder(criteria);

        //then
        assertNotNull(orderResult);
        verify(orderService, times(1)).save(any());
    }

    @Test
    void 사용자_쿠폰할인을_적용하여_주문을_생성한다(){
        //given
        CouponTemplate template = CouponTemplate.builder()
                .id(10L)
                .couponType(CouponType.FLAT)
                .expireDateTime(LocalDateTime.now().plusDays(1))
                .name("test coupon")
                .remainingQuantity(10)
                .build();
        Coupon coupon = new FlatDiscountCoupon(template, 3000);
        FakeUserCoupon userCoupon = new FakeUserCoupon(5L, user, coupon);
        when(userCouponService.findUserCouponById(any())).thenReturn(userCoupon);
        OrderItemCriteria itemCriteria = OrderItemCriteria.of(available.getId(), 4000, 1);
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(1L, 5L, List.of(itemCriteria));

        //when
        OrderResult.Create orderResult = orderFacade.createOrder(criteria);

        //then
        assertNotNull(orderResult);
        assertEquals(1000, orderResult.getPayPrice());
        verify(orderService, times(1)).save(any());
        verify(userCouponService, times(1)).findUserCouponById(any());
    }

    @Test
    void 상품재고가_부족할경우_주문을_생성할수없다(){
        //given
        OrderItemCriteria itemCriteria = OrderItemCriteria.of(available.getId(), 4000, available.stock() + 1);
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(1L, null, List.of(itemCriteria));
        when(itemService.findItemById(1L)).thenReturn(available);

        //when
        assertThrows(InvalidStockException.class, () -> orderFacade.createOrder(criteria));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, -1, -2, -3, -4})
    void 주문상품의_가격과_실제가격이_일치하지않을경우_주문을_생성할수없다(Integer i){
        //given
        OrderItemCriteria itemCriteria = OrderItemCriteria.of(available.getId(), available.price() + i, 1);
        OrderPaymentCriteria criteria = new OrderPaymentCriteria(1L, null, List.of(itemCriteria));
        when(itemService.findItemById(1L)).thenReturn(available);

        //then
        assertThrows(InvalidPriceException.class, () -> orderFacade.createOrder(criteria));
    }

}
