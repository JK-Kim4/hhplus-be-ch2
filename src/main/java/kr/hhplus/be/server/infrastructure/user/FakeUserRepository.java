package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class FakeUserRepository implements UserRepository {

    private Map<Long, User> userMap = new HashMap<>();

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public User insertOrUpdate(User user) {
        userMap.put(user.id(), user);

        return userMap.get(user.id());
    }
}
