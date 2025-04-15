package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.application.order.OrderItemCriteria;
import kr.hhplus.be.server.domain.order.command.OrderCommand;

@Schema(name = "OrderItemRequest: 주문 상품 요청")
public class OrderItemRequest {

    @Schema(name = "itemId", description = "상품 고유 번호", example = "1")
    @NotNull @Positive
    private Long itemId;
    @Schema(name = "price", description = "주문 요청 상품 가격", example = "10000")
    @NotNull @Positive
    private Integer price;
    @Schema(name = "quantity", description = "주문 수량", example = "30")
    @NotNull @Positive
    private Integer quantity;


    public OrderItemRequest() {}

    public OrderItemRequest(Long itemId, Integer price, Integer quantity) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderCommand.OrderItemCreate toCommand(){
        return new OrderCommand.OrderItemCreate(itemId, price, quantity);
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
