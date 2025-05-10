package kr.hhplus.be.server.interfaces.common.exception;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T t) throws Exception;
}
