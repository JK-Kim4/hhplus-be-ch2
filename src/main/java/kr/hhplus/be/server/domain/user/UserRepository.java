package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.point.Point;

import java.util.Optional;


public interface UserRepository {

    void flush();

    void deleteAll();

    User save(User user);

    Point save(Point point);

    Optional<User> findByName(String name);

    Optional<User> findById(Long userId);
}
