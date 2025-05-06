package kr.hhplus.be.server.interfaces.common.lock.redis;

import kr.hhplus.be.server.interfaces.common.lock.DistributedLockCallback;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutor;
import kr.hhplus.be.server.interfaces.common.lock.LockExecutorType;
import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class SpinLockExecutor implements LockExecutor {

    private final RedissonClient redissonClient;

    private static final String UNLOCK_LUA_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "   return redis.call('del', KEYS[1]) " +
            "else " +
            "   return 0 " +
            "end";

    public SpinLockExecutor(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    @Override
    public LockExecutorType getType() {
        return LockExecutorType.SPIN;
    }

    @Override
    public <T> T execute(String key, long waitTime, long leaseTime, DistributedLockCallback<T> action) {
        String lockValue = UUID.randomUUID().toString();
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(waitTime);
        boolean acquired = false;

        try {
            //락 획득 시도 (waitTime 초간 시도)
            while (System.currentTimeMillis() < endTime){

                RBucket<String> bucket = redissonClient.getBucket(key);//전달받은 key로 버킷 생성
                if (bucket.setIfAbsent(lockValue, Duration.ofSeconds(leaseTime))) {
                    acquired = true;
                    break;
                }
                Thread.sleep(50); // 백오프 (짧은 대기 후 재시도)
            }

            if (!acquired) {
                throw new RuntimeException("SpinLock 획득 실패: " + key);
            }

            return action.call();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {

            //락이 획득 되어있다면
            if (acquired) {
                try {
                    redissonClient.getScript().eval(
                            RScript.Mode.READ_WRITE,
                            UNLOCK_LUA_SCRIPT,
                            RScript.ReturnType.INTEGER,
                            Collections.singletonList(key),
                            lockValue
                    );
                } catch (Exception e) {
                    System.err.println("❌ 락 해제 Lua 실패: " + e.getMessage());
                }
            }
        }

    }
}
