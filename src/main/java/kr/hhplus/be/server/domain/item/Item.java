package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    private Long id;
    private String name;
    private ItemPrice price;
    private ItemStock stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public Item(Long id, String name, Integer price, Integer stock) {
        this.id = id;
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

    public boolean hasEnoughStock(Integer quantity) {
        if(quantity >= ItemStock.MAXIMUM_STOCK_QUANTITY){
            throw new InvalidStockException(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY);
        }
        return this.stock.stock() >= quantity;
    }

    public boolean isSamePrice(Integer price){
        if(!this.getPrice().equals(price)){
            throw new IllegalArgumentException("가격이 일치하지않습니다. 확인후 다시 시도해주세요.");
        }

        return true;
    }

    public void updatePrice(Integer updatePrice) {
        this.price = ItemPrice.update(updatePrice);
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(Integer amount) {
        this.stock = this.stock.decrease(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(Integer amount) {
        this.stock = this.stock.increase(amount);
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id)
                && Objects.equals(name, item.name)
                && Objects.equals(price, item.price)
                && Objects.equals(stock, item.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
