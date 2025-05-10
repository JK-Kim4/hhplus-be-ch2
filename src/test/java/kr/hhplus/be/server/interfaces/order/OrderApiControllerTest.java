package kr.hhplus.be.server.interfaces.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class OrderApiControllerTest {

    private final String EXIST_USER_NAME = "hello";
    private Long userId;
    private Long productId;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BalanceService balanceService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        User existUser = User.createWithName(EXIST_USER_NAME);
        userRepository.save(existUser);
        userRepository.flush();

        balanceService.create(BalanceCommand.Create.of(existUser.getId(), BigDecimal.valueOf(100_000)));
        Product product = productRepository.save(Product.create("test-product", BigDecimal.valueOf(10_000), 5));

        userId = existUser.getId();
        productId = product.getId();
    }

    @Test
    void 상품_주문_성공_테스트() throws Exception {
        // Given
        OrderRequest.Order request = OrderRequest.Order.of(
                userId,
                null,
                List.of(OrderRequest.Item.of(productId, BigDecimal.valueOf(10_000), 3))
        );

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNumber())
                .andExpect(jsonPath("$.totalPrice").value(30_000));
    }
}
