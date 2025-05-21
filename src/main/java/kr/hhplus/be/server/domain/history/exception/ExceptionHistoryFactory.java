package kr.hhplus.be.server.domain.history.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

public class ExceptionHistoryFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ExceptionHistory create(String methodName, Object[] parameters, boolean retryable) {
        return ExceptionHistory.of(
                methodName,
                serializeParameters(parameters),
                LocalDateTime.now(),
                retryable);
    }

    private static String serializeParameters(Object[] parameters) {
        try {
            return objectMapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            return Arrays.toString(parameters); // fallback
        }
    }
}
