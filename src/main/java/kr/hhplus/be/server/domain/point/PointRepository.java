package kr.hhplus.be.server.domain.point;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository {
    Optional<Point> findByUserId(Long userId);

    void save(Point point);
}
