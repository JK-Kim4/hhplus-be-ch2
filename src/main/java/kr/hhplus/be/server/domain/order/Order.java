package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.orderItem.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.exception.OrderMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;

    private User orderUser;

    private Payment payment;

    private OrderStatus orderStatus;

    private Integer totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Order() {}

    public Order(User user){
        this.orderUser = user;
        this.orderStatus = OrderStatus.ORDERED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(Long id, User orderUser, Payment payment){

        this.id = id;
        this.orderUser = orderUser;
        this.payment = payment;
        this.orderStatus = OrderStatus.ORDERED;

    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public Integer getTotalPrice() {
        return this.totalPrice;
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
