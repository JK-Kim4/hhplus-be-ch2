package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.point.Point;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "user")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected Point point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User createWithName(String name) {
        if(Objects.isNull(name)) {
            throw new IllegalArgumentException("사용자 이름을 입력해주세요.");
        }

        return new User(name);
    }

    /*Fake 객체 상속을 위해 protected*/
    protected User(String name) {
        this.name = name;
        this.point = Point.createWithUser(this);
    }

    public Integer point(){
        return point.getPointAmount();
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
