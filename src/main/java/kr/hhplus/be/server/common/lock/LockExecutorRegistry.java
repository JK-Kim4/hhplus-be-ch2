package kr.hhplus.be.server.common.lock;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LockExecutorRegistry {

    private final Map<LockExecutorType, LockExecutor> executors;

    public LockExecutorRegistry(List<LockExecutor> executors) {

        this.executors = executors.stream()
                .collect(Collectors.toMap(LockExecutor::getType, Function.identity()));
    }

    public LockExecutor getV2(LockExecutorType type) {
        LockExecutor executor = executors.get(type);
        if (executor == null) {
            throw new IllegalStateException("No LockExecutor found for: " + type);
        }

        return executor;
    }

}
