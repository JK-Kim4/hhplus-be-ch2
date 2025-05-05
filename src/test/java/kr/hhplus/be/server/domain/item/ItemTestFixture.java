package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.domain.FakeItem;

public class ItemTestFixture {

    public static Item createDefaltItem(){
        return new FakeItem();
    }

    public static Item createDefaultItemFixture(){
        return Item.createWithNameAndPriceAndStock(
                "test item",
                5_000,
                10);
    }


    public static Item createItemFixtureWithStock(Integer stock){
        return Item.createWithNameAndPriceAndStock(
                "test item with stock",
                5_000,
                stock
        );
    }

}
