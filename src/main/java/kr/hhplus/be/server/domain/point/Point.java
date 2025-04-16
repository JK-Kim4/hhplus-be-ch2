package kr.hhplus.be.server.domain.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;
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
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Transient
    private PointHistories pointHistories;

    private Point(User user, Integer amount) {
        this.user = user;
        this.amount = amount;
    }

    public User user(){
        return user;
    }

    public static Point create(User user) {

        if(user == null) {
            throw new IllegalArgumentException("사용자 정보를 전달해주세요.");
        }

        return new Point(user, 0);
    }

    public void charge(Integer chargeAmount) {
        if(chargeAmount < MINIMUM_CHARGE_AMOUNT) {
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT);
        }

        if(this.amount + chargeAmount > MAXIMUM_BALANCE) {
            throw new InvalidAmountException(InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED);
        }

        this.amount += chargeAmount;
    }

    public void deduct(Integer deductAmount) {
        if(this.amount < deductAmount){
            throw new InvalidAmountException(InvalidAmountException.INSUFFICIENT_BALANCE);
        }

        this.amount -= deductAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(id, point.id) && Objects.equals(user, point.user) && Objects.equals(amount, point.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, amount);
    }
}
