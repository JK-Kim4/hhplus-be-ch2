package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.interfaces.exception.InvalidStockException;

import java.util.Objects;

public record ItemStock(Integer stock) {

    public static final Integer MAXIMUM_STOCK_QUANTITY = 100_000;
    public static final Integer MINIMUM_STOCK_QUANTITY = 0;

    public static ItemStock createOrDefault(Integer stock) {
        stock = Objects.requireNonNullElse(stock, MINIMUM_STOCK_QUANTITY);

        if (stock < MINIMUM_STOCK_QUANTITY) {
            throw new InvalidStockException(InvalidStockException.INSUFFICIENT_MINIMUM_STOCK_QUANTITY);
        }

        if(stock > MAXIMUM_STOCK_QUANTITY) {
            throw new InvalidStockException(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY);
        }

        return new ItemStock(stock);
    }

    public ItemStock increase(Integer amount) {

        if(amount <= 0){
            throw new InvalidStockException(InvalidStockException.INVALID_INCREASE_QUANTITY);
        }

        return ItemStock.createOrDefault(this.stock + amount);
    }

    public ItemStock decrease(Integer amount) {

        if(amount <= 0){
            throw new InvalidStockException(InvalidStockException.INVALID_DECREASE_QUANTITY);
        }

        if(amount > this.stock){
            throw new InvalidStockException(InvalidStockException.INSUFFICIENT_STOCK_QUANTITY);
        }

        return ItemStock.createOrDefault(this.stock - amount);
    }


}
