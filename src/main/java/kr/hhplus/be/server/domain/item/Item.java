package kr.hhplus.be.server.domain.item;

import jakarta.persistence.*;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Price price;

    @Embedded
    private Stock  stock;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Item(String name) {
        this.name = name;
        this.price = Price.createOrDefault();
        this.stock = Stock.createOrDefault();
    }

    public Item(String name, Integer price) {
        this.name = name;
        this.price = Price.createOrDefault(price);
        this.stock = Stock.createOrDefault();
    }

    public Item(String name, Integer price, Integer stock) {
        this.name = name;
        this.price = Price.createOrDefault(price);
        this.stock = Stock.createOrDefault(stock);
    }

    public Item(Long id, String name, Integer price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = Price.createOrDefault(price);
        this.stock = Stock.createOrDefault(stock);
    }

    public void setPrice(Integer price) {
        this.price = Price.createOrDefault(price);
    }

    public void setStock(Integer stock) {
        this.stock = Stock.createOrDefault(stock);
    }

    public Integer price(){
        return this.price.getPrice();
    }

    public Integer stock(){
        return this.stock.getStock();
    }

    public boolean hasEnoughStock(Integer quantity) {
        if(quantity >= Stock.MAXIMUM_STOCK_QUANTITY){
            throw new InvalidStockException(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY);
        }

        if(quantity > this.stock()){
            throw new InvalidStockException(InvalidStockException.INSUFFICIENT_STOCK_QUANTITY);
        }

        return true;
    }

    public boolean isSamePrice(Integer price){
        if(!this.price().equals(price)){
            throw new InvalidPriceException(
                    String.format(InvalidPriceException.ITEM_PRICE_MISMATCH, this.id));
        }

        return true;
    }

    public void updatePrice(Integer updatePrice) {
        price.updatePrice(updatePrice);
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(Integer amount) {
        this.stock.decrease(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(Integer amount) {
        this.stock.increase(amount);
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
