package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.domain.user.point.Point;
import kr.hhplus.be.server.domain.user.User;

public class FakeUser extends User {

    public FakeUser() {
    }

    public FakeUser(Long id, String name) {
        super(name);
        this.id = id;
    }

    protected void setPoint(Point point) {
        this.point = point;
    }

}
