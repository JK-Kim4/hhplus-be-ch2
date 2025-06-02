package kr.hhplus.be.server.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redisson = null;
        Config config = new Config();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원 추가
        objectMapper.findAndRegisterModules();

        JsonJacksonCodec jsonCodec = new JsonJacksonCodec(objectMapper);
        config.setCodec(jsonCodec);
        config.useSingleServer()
                .setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort)
                .setConnectionPoolSize(100)
                .setConnectionMinimumIdleSize(10)
                .setTimeout(3000);

        redisson = Redisson.create(config);
        return redisson;
    }
}
