package kr.hhplus.be.server.domain.user.pointHistory;

import kr.hhplus.be.server.domain.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class PointHistory {

    private Long id;
    private Long userId;
    private PointHistoryType pointHistoryType;
    private Integer point;
    private LocalDateTime createdAt;

    public PointHistory(){}

    public PointHistory(Long userId, Integer point, PointHistoryType pointHistoryType) {

        this.userId = userId;
        this.point = point;
        this.pointHistoryType = pointHistoryType;
        this.createdAt = LocalDateTime.now();

    }

    public PointHistory(User user, PointHistoryType pointHistoryType) {

        this.userId = user.getId();
        this.point = user.getPoint();
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
