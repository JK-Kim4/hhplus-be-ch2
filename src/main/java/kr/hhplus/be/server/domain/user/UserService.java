package kr.hhplus.be.server.domain.user;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.user.pointHistory.PointHistory;
import kr.hhplus.be.server.domain.user.pointHistory.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.pointHistory.PointHistoryType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserService(
            UserRepository userRepository,
            PointHistoryRepository pointHistoryRepository) {
        this.userRepository = userRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public Integer findPointById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(NoResultException::new);

        return user.point();
    }

    @Transactional
    public Integer charge(UserCommand.Charge chargeCommand) {

        User user = userRepository.findById(chargeCommand.getUserId())
                .orElseThrow(NoResultException::new);

        user.chargePoint(chargeCommand.getAmount());
        pointHistoryRepository.save(new PointHistory(user.id(), chargeCommand.getAmount(), PointHistoryType.CHARGE));

        return user.point();
    }
}
