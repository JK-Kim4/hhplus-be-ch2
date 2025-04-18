package kr.hhplus.be.server.domain.user.point.pointHistory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private LocalDateTime createdAt;

    public PointHistory(Long userId, Integer amount, PointHistoryType pointHistoryType) {

        this.userId = userId;
        this.amount = amount;
        this.pointHistoryType = pointHistoryType;
        this.createdAt = LocalDateTime.now();

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PointHistory that = (PointHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                    pointHistoryType == that.pointHistoryType &&
                        Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, pointHistoryType, amount);
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "point=" + amount +
                ", pointHistoryType=" + pointHistoryType +
                ", userId=" + userId +
                ", id=" + id +
                '}';
    }
}
