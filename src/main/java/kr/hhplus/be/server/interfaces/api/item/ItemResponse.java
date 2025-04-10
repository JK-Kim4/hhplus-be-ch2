package kr.hhplus.be.server.interfaces.api.item;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.item.Item;

public class ItemResponse {

    static public class Detail{

        protected Detail(){}

        @Schema(name = "itemId", description = "아이템 고유 번호", example = "1")
        private Long itemId;

        @Schema(name = "name", description = "상품 명", example = "자전거")
        private String name;

        @Schema(name = "price", description = "상품 가격", example = "10000")
        private Integer price;

        @Schema(name = "stock", description = "상품 재고", example = "5")
        private Integer stock;

        public Detail(Long itemId, String name, Integer price, Integer stock) {
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public Detail(ItemCommand.Response response) {
            Item item  = response.getItem();
            this.itemId = item.getId();
            this.name = item.getName();
            this.price = item.getPrice();
            this.stock = item.getStock();
        }

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

    static public class Rank{

        protected Rank() {
        }

        @Schema(name = "itemId", description = "상품 고유번호", example = "10")
        private Long itemId;

        @Schema(name = "name", description = "상품명", example = "자전거")
        private String name;

        @Schema(name = "price", description = "상품 가격", example = "10000")
        private Integer price;

        @Schema(name = "orderCount", description = "3일간 총 판매 수", example = "55213")
        private Integer orderCount;

        public Rank(Long itemId, String name, Integer price, Integer orderCount) {
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.orderCount = orderCount;
        }

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
}
