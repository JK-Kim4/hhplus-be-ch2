package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;
    public PointRepositoryImpl(PointJpaRepository pointJpaRepository) {

        this.pointJpaRepository = pointJpaRepository;
    }

    public void flush(){
        pointJpaRepository.flush();
    }

    @Override
    public Optional<Point> findById(Long id) {
        return pointJpaRepository.findById(id);
    }

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public void save(Point point) {
        pointJpaRepository.flush();
        pointJpaRepository.save(point);
    }
}
