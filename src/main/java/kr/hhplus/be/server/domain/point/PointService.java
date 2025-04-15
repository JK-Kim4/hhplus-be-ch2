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

    //TODO. Logging Point.충전
    @Transactional
    public void charge(@Valid PointCommand.Charge chargeCommand) {
        Point point = findPointByUserId(chargeCommand.getUserId());
        point.charge(chargeCommand.getAmount());
    }

    //TODO. Logging Point.생성
    @Transactional
    public Point findPointByUserId(Long userId){
        return pointRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Point newPoint = Point.create(userId);
                    pointRepository.save(newPoint);
                    return newPoint;
                });
    }

}
