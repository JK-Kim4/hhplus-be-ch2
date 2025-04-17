package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.domain.item.Item;

public class FakeItem extends Item {

    public FakeItem() {
    }

    public FakeItem(Long id, String name) {
        super(name);
        this.id = id;
    }

    public FakeItem(Long id,String name, Integer price) {
        super(name, price);
        this.id = id;
    }

    public FakeItem(Long id, String name, Integer price, Integer stock) {
        super(name, price, stock);
        this.id = id;
    }
}
