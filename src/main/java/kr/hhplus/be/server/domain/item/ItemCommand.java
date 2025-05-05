package kr.hhplus.be.server.domain.item;

import lombok.Getter;

import java.util.Objects;

public class ItemCommand {

    public static class Item {

        private Long itemId;
        private String name;
        private Integer price;
        private Integer stock;

        public static Item of(Long itemId, Integer price, Integer stock) {
            return new Item(itemId, null, price, stock);
        }

        public static Item from(kr.hhplus.be.server.domain.item.Item item) {
            return new Item(item);
        }

        public Item(Long itemId, String name, Integer price, Integer stock) {
            this.itemId = itemId;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public Item(kr.hhplus.be.server.domain.item.Item item) {
            this.itemId = item.getId();
            this.name = item.getName();
            this.price = item.price();
            this.stock = item.stock();
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(itemId, item.itemId) && Objects.equals(name, item.name) && Objects.equals(price, item.price) && Objects.equals(stock, item.stock);
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemId, name, price, stock);
        }

        @Override
        public String toString() {
            return "Item{" +
                    "itemId=" + itemId +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", stock=" + stock +
                    '}';
        }
    }

    public static class CanOrder {

        private Long ItemId;
        private Integer price;
        private Integer quantity;

        public CanOrder() {}

        public CanOrder(Long itemId, Integer price, Integer quantity) {
            ItemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getItemId() {
            return ItemId;
        }

        public Integer getPrice() {
            return price;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }

    @Getter
    public static class Create {

        private String name;
        private Integer price;
        private Integer stock;

        protected Create(String name, Integer price, Integer stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public static Create of(String name, Integer price, Integer stock) {
            return new Create(name, price, stock);
        }

    }

    @Getter
    public static class Deduct {
        private Long ItemId;
        private Integer quantity;

        public static Deduct of(Long ItemId, Integer quantity) {
            return new Deduct(ItemId, quantity);
        }

        private Deduct(Long ItemId, Integer quantity) {
            this.ItemId = ItemId;
            this.quantity = quantity;
        }
    }

    @Getter
    public static class OrderItem {

        Long itemId;
        Integer price;
        Integer quantity;


        public static OrderItem of(Long itemId, Integer price, Integer quantity) {
            return new OrderItem(itemId, price, quantity);
        }

        private OrderItem(Long itemId, Integer price, Integer quantity) {
            this.itemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
