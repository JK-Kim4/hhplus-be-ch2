package kr.hhplus.be.server.domain.point;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional
    public void charge(@Valid PointCommand.Charge chargeCommand) {
        Point point = findPointOrCreateDefaultByUserId(chargeCommand.getUserId());
        point.charge(chargeCommand.getAmount());
    }

    public PointInfo.Point getPoint(Long userId) {
        Point point = findPointOrCreateDefaultByUserId(userId);
        return PointInfo.Point.from(point);
    }

    private Point findPointOrCreateDefaultByUserId(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Point newPoint = Point.create(userId);
                    pointRepository.save(newPoint);
                    return newPoint;
                });
    }

}
