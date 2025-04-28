package kr.hhplus.be.server.integration.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class RedisConnectionTest {


    @Container
    static final GenericContainer<?> redisContainer =
            new GenericContainer<>(DockerImageName.parse("redis:7"))
                    .withExposedPorts(6379);


    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host",
                redisContainer::getHost);
        registry.add("spring.data.redis.port",
                () -> redisContainer.getMappedPort(6379));
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void redis_연결_및_읽기쓰기_테스트() {
        // given
        String key   = "health:check";
        String value = "OK";

        // when
        stringRedisTemplate.opsForValue().set(key, value);
        String saved = stringRedisTemplate.opsForValue().get(key);

        // then
        assertThat(saved).isEqualTo(value);
    }
}
