package kr.hhplus.be.server.domain.item;

import java.time.LocalDateTime;

public class Item {

    private Long id;

    private String name;

    private ItemPrice price;

    private ItemStock stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected Item() {}

    public Item(String name) {
        this.name = name;
        this.price = ItemPrice.createOrDefault(null);
        this.stock = ItemStock.createOrDefault(null);
    }

    public Item(String name, Integer price) {
        this.name = name;
        this.price = ItemPrice.createOrDefault(price);
        this.stock = ItemStock.createOrDefault(null);
    }

    public Item(String name, Integer price, Integer stock) {
        this.name = name;
        this.price = ItemPrice.createOrDefault(price);
        this.stock = ItemStock.createOrDefault(stock);
    }

    public Integer price(){
        return this.price.price();
    }

    public Integer stock(){
        return this.stock.stock();
    }

    public void updatePrice(Integer updatePrice) {
        this.price = ItemPrice.update(updatePrice);
    }

    public void decreaseStock(Integer amount) {
        this.stock = this.stock.decrease(amount);
    }

    public void increaseStock(Integer amount) {
        this.stock = this.stock.increase(amount);
    }
}
