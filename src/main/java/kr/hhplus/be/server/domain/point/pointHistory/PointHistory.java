package kr.hhplus.be.server.domain.point.pointHistory;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
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

    @Column(name = "point")
    private Integer point;

    @Column
    private LocalDateTime createdAt;

    public PointHistory(Long userId, Integer point, PointHistoryType pointHistoryType) {

        this.userId = userId;
        this.point = point;
        this.pointHistoryType = pointHistoryType;
        this.createdAt = LocalDateTime.now();

    }

    public PointHistory(User user, PointHistoryType pointHistoryType) {

        this.userId = user.getId();
        this.point = user.getPoint().getAmount();
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
                        Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, pointHistoryType, point);
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "point=" + point +
                ", pointHistoryType=" + pointHistoryType +
                ", userId=" + userId +
                ", id=" + id +
                '}';
    }
}
