package kr.hhplus.be.server.common.log;

public class LogContextHolder {

    private static final ThreadLocal<LogContext> holder = new ThreadLocal<>();

    public static void setContext(LogContext context) {
        holder.set(context);
    }

    public static LogContext getContext() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }

}
