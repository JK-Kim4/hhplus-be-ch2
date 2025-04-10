package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.interfaces.exception.OrderMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private User orderUser;
    private UserCoupon coupon;
    private Payment payment;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private Integer finalPaymentPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order() {}

    public Order(User user){
        this.orderUser = user;
        this.orderStatus = OrderStatus.ORDER_CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(Long id, User orderUser, Payment payment){
        this.id = id;
        this.orderUser = orderUser;
        this.payment = payment;
        this.orderStatus = OrderStatus.ORDER_CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public Integer getTotalPrice() {
        return this.totalPrice;
    }

    public Integer getFinalPaymentPrice() {
        return finalPaymentPrice;
    }

    public Long getId() {
        return id;
    }

    public User getOrderUser() {
        return orderUser;
    }

    public Payment getPayment() {
        return payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void registerPayment(Payment payment) {
        this.payment = payment;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void calculateTotalPrice(List<OrderItem> orderItems) {

        Integer totalPrice = 0;

        for(OrderItem orderItem : orderItems) {
            if (orderItem.getOrder() != this){
                throw new OrderMismatchException();
            }
            totalPrice += orderItem.calculatePrice();
        }

        this.totalPrice = totalPrice;
        this.finalPaymentPrice = totalPrice;
    }

    public void applyCoupon(UserCoupon userCoupon) {

        if(this.orderUser != userCoupon.getUser()) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
        }

        this.coupon = userCoupon;
        this.finalPaymentPrice = coupon.discount(this.totalPrice);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderUser, order.orderUser) && Objects.equals(payment, order.payment) && orderStatus == order.orderStatus && Objects.equals(totalPrice, order.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderUser, payment, orderStatus, totalPrice);
    }

}
