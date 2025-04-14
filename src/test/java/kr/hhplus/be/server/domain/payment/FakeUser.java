package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;

public class FakeUser extends User {

    public FakeUser() {
    }

    public FakeUser(Long id, String name) {
        super(id, name);
    }

    public FakeUser(String name) {
        super(name);
    }

    protected void setPoint(Point point) {
        this.point = point;
    }
}
