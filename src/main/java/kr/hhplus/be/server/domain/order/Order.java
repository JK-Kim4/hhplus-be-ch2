package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "Orders")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_coupon_id")
    private Long userCouponId;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderItems orderItems;

    @Column(name = "total_price")
    protected Integer totalPrice;

    @Column(name = "final_payment_price")
    private Integer finalPaymentPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order createWithItems(User user, List<OrderItem> orderItemList) {
        validationUser(user);
        validationOrderItems(orderItemList);

        return new Order(user, orderItemList);
    }

    public Order(User user, List<OrderItem> orderItemList){
        this.user = user;
        user.addOrder(this);
        this.orderItems = new OrderItems(this, orderItemList);
        calculateTotalPrice();
        this.orderStatus = OrderStatus.ORDER_CREATED;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems.getOrderItems();
    }

    public Long getOrderUserId(){
        return user.getId();
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
        if(!this.equals(payment.getOrder())){
            throw new IllegalArgumentException("주문 정보가 일치하지않습니다.");
        }
        this.payment = payment;
        this.orderStatus = OrderStatus.PAYMENT_WAITING;
    }

    public void applyCoupon(UserCoupon userCoupon) {
        if (Objects.isNull(userCoupon)) {
            return;
        }
        userCoupon.isUsable(LocalDate.now(), user);
        this.userCouponId = userCoupon.getId();
        this.finalPaymentPrice = userCoupon.discount(this.totalPrice);
        userCoupon.updateUsedCouponInformation(this);
    }

    public void deductOrderItemStock() {
        orderItems.deductStock();
    }

    private static void validationUser(User user) {
        if(Objects.isNull(user)) {
            throw new IllegalArgumentException("사용자 정보가 존재하지않습니다.");
        }

        user.canCreateOrder();
    }

    private static void validationOrderItems(List<OrderItem> orderItemList) {
        if(orderItemList == null || orderItemList.isEmpty()) {
            throw new IllegalArgumentException("구매하실 상품이 존재하지않습니다.");
        }
    }
}
