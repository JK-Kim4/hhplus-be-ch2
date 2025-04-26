package kr.hhplus.be.server.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.integration.support.DatabaseCleanup;
import kr.hhplus.be.server.integration.support.InitialTestData;
import kr.hhplus.be.server.integration.support.SampleValues;
import kr.hhplus.be.server.interfaces.api.order.OrderItemRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

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
    void 주문_결제를_수행할_수_있다() throws Exception {
        //given
        List<OrderItemRequest> orderItemRequests = Arrays.asList(
                new OrderItemRequest(sampleValues.items.car.getId(), sampleValues.items.car.price(), 1),
                new OrderItemRequest(sampleValues.items.truck.getId(), sampleValues.items.truck.price(), 1),
                new OrderItemRequest(sampleValues.items.book.getId(), sampleValues.items.book.price(), 1)
        );

        OrderRequest.OrderPayment request = new OrderRequest.OrderPayment(sampleValues.user.getId(), null, orderItemRequests);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
