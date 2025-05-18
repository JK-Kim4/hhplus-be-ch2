package kr.hhplus.be.server.interfaces.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.balance.BalanceCommand;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.api.payment.PaymentRequest;
import kr.hhplus.be.server.support.DatabaseCleanup;
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
public class PaymentApiControllerTest {

    private final String EXIST_USER_NAME = "hello";
    private Long userId;
    private Long orderId;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BalanceService balanceService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderFacade orderFacade;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        databaseCleanup.truncate();

        User existUser = User.createWithName(EXIST_USER_NAME);
        userRepository.save(existUser);
        userRepository.flush();

        balanceService.create(BalanceCommand.Create.of(existUser.getId(), BigDecimal.valueOf(100_000)));
        Product product = productRepository.save(Product.create("test-product", BigDecimal.valueOf(10_000), 5));

        OrderResult.Create order = orderFacade.order(OrderCriteria.Create.of(existUser.getId(), null,
                List.of(OrderCriteria.Items.of(product.getId(), BigDecimal.valueOf(10_000), 3))));

        userId = existUser.getId();
        orderId = order.getOrderId();
    }

    @Test
    void 결제_성공_테스트() {
        // Given
        PaymentRequest.Pay request = PaymentRequest.Pay.of(orderId, userId);

        // When & Then
        try {
            mockMvc.perform(post("/api/v1/payments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.paymentId").isNumber())
                    .andExpect(jsonPath("$.paidAmount").value(30_000));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
