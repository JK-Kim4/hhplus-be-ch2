package kr.hhplus.be.server.domain.order;

public class Order {

    private Long id;

    private User orderUser;

    private OrderStatus orderStatus;

    private Integer totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Order() {}

    public Order(
            Long id, User orderUser,
            OrderStatus orderStatus, Integer totalPrice,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderUser = orderUser;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void calculateTotalPrice() {

    }

}
