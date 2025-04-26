package kr.hhplus.be.server.domain.user.point;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private PointHistoryType pointHistoryType;

    @Column(name = "amount")
    private Integer amount;

    @Column
    private LocalDateTime actionAt;

    public static PointHistory create(Long userId, PointHistoryType pointHistoryType, Integer amount) {
        return new PointHistory(userId, pointHistoryType, amount);
    }

    private PointHistory(Long userId, PointHistoryType pointHistoryType, Integer amount) {

        this.userId = userId;
        this.amount = amount;
        this.pointHistoryType = pointHistoryType;
        this.actionAt = LocalDateTime.now();

    }
}
