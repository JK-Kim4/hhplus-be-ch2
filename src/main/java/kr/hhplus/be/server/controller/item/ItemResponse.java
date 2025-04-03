package kr.hhplus.be.server.controller.item;

import io.swagger.v3.oas.annotations.media.Schema;

public class ItemResponse {

    @Schema(description = "아이템 고유 번호", example = "1")
    private Long itemId;
    @Schema(description = "상품 명", example = "자전거")
    private String name;
    @Schema(description = "상품 가격", example = "10000")
    private Integer price;
    @Schema(description = "상품 재고", example = "5")
    private Integer stock;

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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
