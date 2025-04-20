package kr.hhplus.be.server.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    protected Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Price price;

    @Embedded
    private Stock  stock;

    public static Item createWithNameAndPrice(String name, Integer price) {
        validateName(name);
        validatePrice(price);
        return new Item(name, price);
    }

    public static Item createWithNameAndPriceAndStock(String name, Integer price, Integer stock) {
        validateName(name);
        validatePrice(price);
        validateStock(stock);
        return new Item(name, price, stock);
    }

    protected Item(String name, Integer price) {
        this.name = name;
        this.price = Price.createOrDefault(price);
        this.stock = Stock.createOrDefault();
    }

    protected Item(String name, Integer price, Integer stock) {
        this.name = name;
        this.price = Price.createOrDefault(price);
        this.stock = Stock.createOrDefault(stock);
    }

    public boolean hasEnoughStock(Integer quantity) {
        return quantity <= this.stock();
    }

    public boolean isSamePrice(Integer price){
        return price.equals(this.price());
    }

    public void updatePrice(Integer updatePrice) {
        price.updatePrice(updatePrice);
    }

    public void decreaseStock(Integer amount) {
        this.stock.decrease(amount);
    }

    public void increaseStock(Integer amount) {
        this.stock.increase(amount);
    }

    public Integer price(){
        return this.price.getPrice();
    }

    public Integer stock(){
        return this.stock.getStock();
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
    }

    private static void validatePrice(Integer price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }

    private static void validateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
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
                '}';
    }
}
