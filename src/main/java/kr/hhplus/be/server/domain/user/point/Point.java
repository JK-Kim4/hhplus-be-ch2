package kr.hhplus.be.server.domain.user.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.common.exception.InvalidAmountException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    public static final Integer MINIMUM_CHARGE_AMOUNT = 100;
    public static final Integer MAXIMUM_BALANCE = 100_000_000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "pointAmount", nullable = false)
    private Integer pointAmount;

    @Version
    private Integer version;

    public static Point createWithUser(User user){
        //Point 객체 생성을 위한 필수 파라미터 검증
        if(Objects.isNull(user)){
            throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다.");
        }

        return new Point(user);
    }

    private Point(User user){

        if(Objects.isNull(user)) {
            throw new IllegalArgumentException("사용자 정보가 존재하지않습니다.");
        }

        this.user = user;
        this.pointAmount = 0;
    }

    public User user(){
        return user;
    }

    public void charge(Integer chargeAmount) {
        if(chargeAmount < MINIMUM_CHARGE_AMOUNT) {
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT);
        }

        if(this.pointAmount + chargeAmount > MAXIMUM_BALANCE) {
            throw new InvalidAmountException(InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED);
        }

        this.pointAmount += chargeAmount;
    }

    public void deduct(Integer deductAmount) {
        if(this.pointAmount < deductAmount){
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_BALANCE);
        }
        this.pointAmount -= deductAmount;
    }

    //TODO. 포인트 사용 내역 저장
    /*public void registerHistory(PointHistory pointHistory) {
        this.pointHistories.addPointHistory(pointHistory);
    }*/

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(id, point.id) && Objects.equals(user, point.user) && Objects.equals(pointAmount, point.pointAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, pointAmount);
    }

    @Override
    public String toString() {
        return "Point{" +
                "pointAmount=" + pointAmount +
                ", id=" + id +
                '}';
    }

    public void setUser(User user) {
        this.user = user;
    }
}
