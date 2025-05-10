package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Getter
@Table(name = "orders", indexes = {
        @Index(name = "idx_join_user_coupon_id", columnList = "user_coupon_id"),
        @Index(name = "idx_join_user_id", columnList = "user_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_coupon_id")
    private Long userCouponId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private OrderItems orderItems;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_amount"))
    private Price totalAmount;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "final_amount"))
    private Price finalAmount;

    private LocalDateTime orderedAt;

    public static Order create(Long userId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 없습니다.");
        }

        Order order = new Order();
        order.userId = userId;
        order.status = OrderStatus.ORDER_CREATED;
        order.orderedAt = LocalDateTime.now();

        order.orderItems = new OrderItems(items, order);
        order.calculateTotalAmount();

        return order;
    }

    public void completePayment() {
        this.status = OrderStatus.PAID;
    }

    public void applyCoupon(UserCoupon userCoupon) {
        this.userCouponId = userCoupon.getId();
        BigDecimal discountAmount = userCoupon.calculateDiscountAmount(this.totalAmount.getAmount());
        this.finalAmount = this.totalAmount.subtractDiscountAmount(discountAmount);
    }

    public void calculateTotalAmount() {
        this.totalAmount = this.orderItems.calculateTotalAmount();
        this.finalAmount = this.orderItems.calculateTotalAmount();
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount.getAmount();
    }

    public BigDecimal getFinalAmount() {
        return this.finalAmount.getAmount();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getItems() ;
    }
}
