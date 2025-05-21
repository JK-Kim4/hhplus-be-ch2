package kr.hhplus.be.server.infrastructure.lock;

public enum LockExecutorType {
    REDISSON,
    PUBSUB,
    SPIN;
}
