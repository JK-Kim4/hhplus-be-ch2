package kr.hhplus.be.server.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class E2ETestSupport extends TestRepositorySupport{

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    RedisCleanup redisCleanup;

    @BeforeEach
    void cleanDatabase() {
        databaseCleanup.truncate();
        redisCleanup.truncate();
    }

}
