package kr.hhplus.be.server.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.support.DatabaseCleanup;
import kr.hhplus.be.server.integration.support.InitialTestData;
import kr.hhplus.be.server.integration.support.SampleValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    InitialTestData initialTestData;

    @Autowired
    DatabaseCleanup cleanup;

    @Autowired
    UserRepository userRepository;

    SampleValues sampleValues;

    @BeforeEach
    void setUp() {
        cleanup.truncateAllTables();
        sampleValues = initialTestData.load();
    }

    @Test
    void 사용자는_포인트를_충전할_수_있다() throws Exception {
        // when
        mockMvc.perform(patch("/api/users/{userId}/point", sampleValues.user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(50_000)))
                .andExpect(status().isOk());
        User user = userRepository.findById(sampleValues.user.getId()).get();

        //then
        assertEquals(sampleValues.user.point() + 50_000, user.point());
    }

    @Test
    void 사용자는_포인트를_조회할_수_있다() throws Exception {
        //when
        mockMvc.perform(get("/api/users/{userId}/point", sampleValues.user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(sampleValues.user.getId()))
                .andExpect(jsonPath("$.point").value(sampleValues.user.point()));
    }
}
