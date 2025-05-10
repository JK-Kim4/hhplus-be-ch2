package kr.hhplus.be.server.infrastructure.user;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name = :name")
    Optional<User> findByName(@Param("name") String name);
}
