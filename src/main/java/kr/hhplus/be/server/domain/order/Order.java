package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long userId;
    private Long userCouponId;

    private User user;
    private UserCoupon userCoupon;

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
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getUserCouponId() {
        return userCouponId;
    }

    public Payment getPayment() {
        return payment;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getFinalPaymentPrice() {
        return finalPaymentPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void calculateTotalPrice() {
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

        if(!userCoupon.isCouponOwner(userId)) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
        }

        this.userCouponId = userCoupon.getId();
        this.finalPaymentPrice = userCoupon.discount(this.totalPrice);
        userCoupon.updateUsedFlag(true);

    }

    public void registerOrderItems(OrderItems orderItems) {
        this.orderItems = orderItems;
        orderItems.setOrder(this);
    }
}
