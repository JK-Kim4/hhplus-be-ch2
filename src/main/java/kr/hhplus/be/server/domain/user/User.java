package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;


public class User {

    private Long id;

    private String name;

    private UserPoint point;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        this.point = UserPoint.createOrDefault(null);
    }

    public User(Long id, String name, Integer point) {
        this.id = id;
        this.name = name;
        this.point = UserPoint.createOrDefault(point);
    }


    public Integer point() {
        return this.point.getPoint();
    }

    public void chargePoint(Integer chargePoint) {
        this.point = this.point.charge(chargePoint);
    }

    public void deductPoint(Integer deductPoint) {
        this.point = this.point.deduct(deductPoint);
    }
}
