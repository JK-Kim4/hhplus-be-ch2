package kr.hhplus.be.server.controller.item;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ItemRankResponse: 판매 순위 상위권 상품 응답")
public class ItemRankResponse {

    @Schema(name = "itemId", description = "상품 고유번호", example = "10")
    private Long itemId;

    @Schema(name = "name", description = "상품명", example = "자전거")
    private String name;

    @Schema(name = "price", description = "상품 가격", example = "10000")
    private Integer price;

    @Schema(name = "orderCount", description = "3일간 총 판매 수", example = "55213")
    private Integer orderCount;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}
