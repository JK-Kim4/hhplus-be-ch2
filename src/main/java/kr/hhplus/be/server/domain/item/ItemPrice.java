package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;

import java.util.Objects;

public record ItemPrice(Integer price) {

    public static final Integer MAXIMUM_ITEM_PRICE = 100_000_000;
    public static final Integer MINIMUM_ITEM_PRICE = 100;

    public static ItemPrice createOrDefault(Integer price) {
        price = Objects.requireNonNullElse(price, MINIMUM_ITEM_PRICE);

        if(price > MAXIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.OVER_MAXIMUM_PRICE);
        }

        if(price < MINIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE);
        }

        return new ItemPrice(price);
    }


    public static ItemPrice update(Integer price) {

        if(price < MINIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE);
        }

        if(price > MAXIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.OVER_MAXIMUM_PRICE);
        }

        return ItemPrice.createOrDefault(price);
    }
}
