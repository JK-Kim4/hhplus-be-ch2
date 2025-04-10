package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.order.OrderItemCriteria;
import kr.hhplus.be.server.domain.order.command.OrderItemCreateCommand;

@Schema(name = "OrderItemRequest: 주문 상품 요청")
public class OrderItemRequest {

    @Schema(name = "itemId", description = "상품 고유 번호", example = "1")
    private Long itemId;
    @Schema(name = "price", description = "주문 요청 상품 가격", example = "10000")
    private Integer price;
    @Schema(name = "quantity", description = "주문 수량", example = "30")
    private Integer quantity;


    public OrderItemRequest() {}

    public OrderItemRequest(Long itemId, Integer price, Integer quantity) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItemCreateCommand toCommand(){
        return new OrderItemCreateCommand(itemId, price, quantity);
    }

    public OrderItemCriteria toCriteria(){
        return new OrderItemCriteria(itemId, price, quantity);
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
