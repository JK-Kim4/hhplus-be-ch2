package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.point.Point;
import kr.hhplus.be.server.domain.user.point.PointHistory;

import java.util.List;
import java.util.Optional;


public interface UserRepository {

    void flush();

    void deleteAll();

    User save(User user);

    Point save(Point point);

    void savePointHistory(PointHistory pointHistory);

    Optional<User> findByName(String name);

    Optional<User> findById(Long userId);

    List<User> findAll();
}
