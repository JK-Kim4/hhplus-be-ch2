package kr.hhplus.be.server.common.exception;

public class DistributedLockException extends RuntimeException {
    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedLockException(String message) {
        super(message);
    }
}
