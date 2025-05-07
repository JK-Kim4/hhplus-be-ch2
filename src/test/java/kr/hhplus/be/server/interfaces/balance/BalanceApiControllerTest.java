package kr.hhplus.be.server.interfaces.balance;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.user.UserCriteria;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.UserResult;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.api.balance.BalanceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class BalanceApiControllerTest {

    private final String EXIST_USER_NAME = "hello";
    private Long userId;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserFacade userFacade;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        UserResult.Create create = userFacade.create(UserCriteria.Create.of(EXIST_USER_NAME));
        userId = create.getUserId();
        userRepository.flush();
    }

    @Test
    void 사용자_잔고_충전_테스트() throws Exception {
        // Given
        BalanceRequest.Charge request = BalanceRequest.Charge.builder()
                .userId(userId)
                .chargePoint(BigDecimal.valueOf(10_000))
                .build();

        // When & Then
        mockMvc.perform(patch("/api/v1/balances/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.nowPoint").value(10_000));
    }
}
