package kr.hhplus.be.server.infrastructure.lock;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LockExecutorRegistry {

    private final Map<LockExecutorType, LockCallBack.LockExecutor> executors;

    public LockExecutorRegistry(List<LockCallBack.LockExecutor> executors) {

        this.executors = executors.stream()
                .collect(Collectors.toMap(LockCallBack.LockExecutor::getType, Function.identity()));
    }

    public LockCallBack.LockExecutor get(LockExecutorType type) {
        LockCallBack.LockExecutor executor = executors.get(type);
        if (executor == null) {
            throw new IllegalStateException("No LockExecutor found for: " + type);
        }

        return executor;
    }

}
