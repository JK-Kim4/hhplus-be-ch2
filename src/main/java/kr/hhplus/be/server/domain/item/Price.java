package kr.hhplus.be.server.domain.item;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price implements Serializable {

    public static final Integer MAXIMUM_ITEM_PRICE = 100_000_000;
    public static final Integer MINIMUM_ITEM_PRICE = 100;

    private Integer price;

    public Price(Integer price) {
        this.price = price;
    }

    public static Price createOrDefault(Integer price) {
        price = Objects.requireNonNullElse(price, MINIMUM_ITEM_PRICE);


        if(price > MAXIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.OVER_MAXIMUM_PRICE);
        }

        if(price < MINIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE);
        }

        return new Price(price);
    }

    public void updatePrice(Integer price) {
        if(price < MINIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE);
        }

        if(price > MAXIMUM_ITEM_PRICE) {
            throw new InvalidPriceException(InvalidPriceException.OVER_MAXIMUM_PRICE);
        }

        this.price = price;
    }
}
