package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepository extends JpaRepository<Point, Long> {
}
