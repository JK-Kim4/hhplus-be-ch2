package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "point"))
    private Point point;

    @Version
    private Long version;

    @Builder
    private Balance(Long id, Long userId, Point point) {
        validation(userId, point);

        this.id = id;
        this.userId = userId;
        this.point = point;
    }

    public static Balance create(Long userId, Point point) {
        return Balance.builder()
                .userId(userId)
                .point(point)
                .build();
    }

    public void charge(BigDecimal point) {
        this.point.charge(point);
    }

    public void deduct(BigDecimal deductPoint) {
        this.point.deduct(deductPoint);
    }

    public void validation(Long userId, Point point){
        if (userId == null){
            throw new IllegalArgumentException("사용자 정보가 누락되었습니다.");
        }

        if(point == null){
            this.point = Point.createDefault();
        }
    }
}
