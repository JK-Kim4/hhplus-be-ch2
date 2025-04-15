package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    private Long id;
    private Long userId;
    private Long userCouponId;
    private Payment payment;
    private OrderStatus orderStatus;
    private OrderItems orderItems;
    private Integer totalPrice;
    private Integer finalPaymentPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order of(Long userId, Long userCouponId, List<OrderItem> orderItems) {
        return new Order(userId, userCouponId, orderItems);
    }

    public Order(Long userId, Long userCouponId, List<OrderItem> orderItems) {
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderStatus = OrderStatus.ORDER_CREATED;
        this.orderItems = new OrderItems(orderItems);
    }

    public Order(User user){
        this.userId = user.getId();
        //this.user = user;
    }

    public void calculateTotalPrice() {
        if(orderItems.empty()){
            throw new IllegalArgumentException("주문 상품이 존재하지않습니다.");
        }
        this.totalPrice = orderItems.calculateTotalPrice();
        this.finalPaymentPrice = orderItems.calculateTotalPrice();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void registerPayment(Payment payment) {
        this.payment = payment;
    }

    public void applyCoupon(UserCoupon userCoupon) {
        userCoupon.isUsable(LocalDateTime.now(), userId);
        this.userCouponId = userCoupon.getId();
        this.finalPaymentPrice = userCoupon.discount(this.totalPrice);
        userCoupon.updateUsedFlag(true);

    }

    public void registerOrderItems(OrderItems orderItems) {
        this.orderItems = orderItems;
        orderItems.setOrder(this);
    }

    public void addOderItems(List<OrderItem> orderItems) {
        this.orderItems = new OrderItems(orderItems, this);
    }

    public void deductOrderItemStock() {
        orderItems.deductStock();
    }
}
