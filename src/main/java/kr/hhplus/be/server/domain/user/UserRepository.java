package kr.hhplus.be.server.domain.user;

import java.util.Optional;

public interface UserRepository {

    void flush();

    User save(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByName(String name);
}
