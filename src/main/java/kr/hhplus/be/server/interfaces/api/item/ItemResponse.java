package kr.hhplus.be.server.interfaces.api.item;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.item.ItemCommand;

public class ItemResponse {

    static public class Item{

        @Schema(name = "itemId", description = "아이템 고유 번호", example = "1")
        private Long itemId;

        @Schema(name = "name", description = "상품 명", example = "자전거")
        private String name;

        @Schema(name = "price", description = "상품 가격", example = "10000")
        private Integer price;

        @Schema(name = "stock", description = "상품 재고", example = "5")
        private Integer stock;

        public static Item from(ItemCommand.Item item) {
            return new Item(item);
        }

        private Item(ItemCommand.Item item) {
            this.itemId = item.getItemId();
            this.name = item.getName();
            this.price = item.getPrice();
            this.stock = item.getStock();
        }

        private Item(Long itemId, String name, Integer price, Integer stock) {
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public Long getItemId() {
            return itemId;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }

        public Integer getStock() {
            return stock;
        }
    }

}
