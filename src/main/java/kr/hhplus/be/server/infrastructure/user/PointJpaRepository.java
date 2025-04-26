package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.point.Point;
import kr.hhplus.be.server.domain.user.point.PointHistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

    @Modifying
    @Query("insert into PointHistory (userId, pointHistoryType, amount ) values (:userId, :pointHistoryType, :amount) ")
    void savePointHistory(
            @Param("userId") Long userId,
            @Param("pointHistoryType")PointHistoryType pointHistoryType,
            @Param("amount") Integer amount);
}
