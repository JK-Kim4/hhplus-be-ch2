package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.point.Point;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;
    private String name;
    protected Point point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name) {
        this.name = name;
    }

    public User(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer point(){
        return point.getAmount();
    }


    public void chargePoint(Integer amount) {
        this.point.charge(amount);
    }

    public void deductPoint(Integer amount) {
        this.point.deduct(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
