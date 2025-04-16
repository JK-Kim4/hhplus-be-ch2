package kr.hhplus.be.server.domain.point;

import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointService(
            PointRepository pointRepository,
            UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    public void flush(){
        pointRepository.flush();
    }

    //TODO. Logging Point.충전
    @Transactional
    public void charge(@Valid PointCommand.Charge chargeCommand) {
        Point point = findPointByUserId(chargeCommand.getUserId());
        point.charge(chargeCommand.getAmount());
        pointRepository.save(point);
    }

    //TODO. Logging Point.생성
    @Transactional
    public Point findPointByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        return pointRepository.findByUserId(userId)
                .orElseGet(() -> Point.create(user));
    }

}
