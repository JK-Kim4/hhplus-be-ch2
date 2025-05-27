package kr.hhplus.be.server.interfaces.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.api.UserRequest;
import kr.hhplus.be.server.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class UserApiControllerTest {

    private final String EXIST_USER_NAME = "hello";

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setup() {
        databaseCleanup.truncate();
        User existUser = User.createWithName(EXIST_USER_NAME);
        userRepository.save(existUser);
        userRepository.flush();

    }

    @Test
    void 사용자_생성_성공_테스트() throws Exception {
        // Given
        UserRequest.Create request = UserRequest.Create.of("test-user");

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.username").value("test-user"))
                .andExpect(jsonPath("$.point").value(0));
    }

    @Test
    void 이미_등록된_이름의경우_사용자_생성요청_실패() throws Exception {
        // Given
        UserRequest.Create request = UserRequest.Create.of(EXIST_USER_NAME);

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
