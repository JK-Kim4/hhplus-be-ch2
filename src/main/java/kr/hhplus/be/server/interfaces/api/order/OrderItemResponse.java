package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderItemResponse {

    @Schema(name = "itemId", description = "상품 고유번호", examples = "1")
    private Long itemId;

    @Schema(name = "orderId", description = "주문 고유번호", examples = "4")
    private Long orderId;

    @Schema(name = "orderItemId", description = "주문 상품 고유번호", examples = "205")
    private Long orderItemId;

    @Schema(name = "price", description = "주문 생성 시 상품 가격", examples = "10000")
    private Integer price;

    @Schema(name = "quantity", description = "주문 수량", examples = "40")
    private Integer quantity;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
