package kr.hhplus.be.server.domain.item;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock implements Serializable {

    public static final Integer MAXIMUM_STOCK_QUANTITY = 100_000;
    public static final Integer MINIMUM_STOCK_QUANTITY = 0;

    private Integer stock;

    public Stock(Integer stock) {
        this.stock = stock;
    }

    public static Stock createOrDefault(Integer stock){
        stock = Objects.requireNonNullElse(stock, MINIMUM_STOCK_QUANTITY);

        if (stock < MINIMUM_STOCK_QUANTITY) {
            throw new InvalidStockException(InvalidStockException.INSUFFICIENT_MINIMUM_STOCK_QUANTITY);
        }

        if(stock > MAXIMUM_STOCK_QUANTITY) {
            throw new InvalidStockException(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY);
        }

        return new Stock(stock);
    }

    public static Stock createOrDefault(){
        return new Stock(MINIMUM_STOCK_QUANTITY);
    }


    public void increase(Integer amount) {
        if(amount <= 0){
            throw new InvalidStockException(InvalidStockException.INVALID_INCREASE_QUANTITY);
        }

        if(amount + stock > MAXIMUM_STOCK_QUANTITY){
            throw new InvalidStockException(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY);
        }

        this.stock += amount;
    }

    public void decrease(Integer amount) {
        if(amount <= 0){
            throw new InvalidStockException(InvalidStockException.INVALID_DECREASE_QUANTITY);
        }

        if(amount > this.stock){
            throw new InvalidStockException(InvalidStockException.INSUFFICIENT_STOCK_QUANTITY);
        }

        this.stock -= amount;
    }

}
