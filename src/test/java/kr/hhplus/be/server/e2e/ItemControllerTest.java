package kr.hhplus.be.server.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.integration.support.DatabaseCleanup;
import kr.hhplus.be.server.integration.support.InitialTestData;
import kr.hhplus.be.server.integration.support.SampleValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    InitialTestData initialTestData;

    @Autowired
    DatabaseCleanup databaseCleanup;

    SampleValues sampleValues;

    @BeforeEach
    void setUp(){
        databaseCleanup.truncateAllTables();
        sampleValues = initialTestData.load();
    }

    @Test
    void 등록된_상품을_조회할수있다() throws Exception {
        mockMvc.perform(get("/api/items/{itemId}", sampleValues.items.truck.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(sampleValues.items.truck.getId()))
                .andExpect(jsonPath("$.price").value(sampleValues.items.truck.price()))
                .andExpect(jsonPath("$.stock").value(sampleValues.items.truck.stock()))
                .andExpect(jsonPath("$.name").value(sampleValues.items.truck.getName()));
    }
}
