package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.domain.item.Item;

public class FakeItem extends Item {

    public FakeItem() {
        super(1L, "test item", 5000, 10);
    }
}
