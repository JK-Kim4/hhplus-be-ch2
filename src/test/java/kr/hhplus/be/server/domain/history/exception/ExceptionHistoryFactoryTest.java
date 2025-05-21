package kr.hhplus.be.server.domain.history.exception;

import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExceptionHistoryFactoryTest {

    @Test
    void 파라미터를_전달받아_예외이력_객체를_생성한다(){
        // given
        String methodName = "testMethod";
        Object[] parameters = new Object[]{"param1", "param2"};
        boolean retryable = true;

        // when
        ExceptionHistory exceptionHistory = ExceptionHistoryFactory.create(methodName, parameters, retryable);

        // then
        assertNotNull(exceptionHistory);
    }

    @Test
    void 생성된_예외이력_객체에_파라미터는_직렬화된_문자열로_저장된다() throws JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String methodName = "testMethod";
        Object[] parameters = new Object[]{"param1", "param2"};
        boolean retryable = true;

        // when
        ExceptionHistory exceptionHistory = ExceptionHistoryFactory.create(methodName, parameters, retryable);

        // then
        assertEquals(objectMapper.writeValueAsString(parameters), exceptionHistory.getSerializedParameter());
    }
}
