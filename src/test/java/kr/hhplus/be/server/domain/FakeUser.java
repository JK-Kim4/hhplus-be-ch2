package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.point.Point;

public class FakeUser extends User {

    public FakeUser() {
    }

    public FakeUser(Long id, String name) {
        super(name);
        orders = new Orders();
        this.id = id;
    }

    protected void setPoint(Point point) {
        this.point = point;
    }
}
