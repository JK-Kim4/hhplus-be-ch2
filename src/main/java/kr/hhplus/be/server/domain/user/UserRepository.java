package kr.hhplus.be.server.domain.user;

import java.util.Optional;


public interface UserRepository {

    void deleteAll();

    User save(User user);

    Optional<User> findByName(String name);

    Optional<User> findById(Long userId);
}
