package kr.hhplus.be.server.common.lock;

public enum LockExecutorType {
    REDISSON,
    PUBSUB,
    SPIN;
}
