package kr.hhplus.be.server.interfaces.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.DiscountPolicy;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.api.coupon.CouponRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CouponApiControllerTest {

    private Long userId;
    private Long couponId;
    private LocalDate expiredAt;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        LocalDate exdatetime = LocalDate.now().plusDays(3);

        User user = userRepository.save(User.createWithName("test"));
        Coupon coupon = couponRepository.save(Coupon.create(
                "10_000원 할인 테스트쿠폰",
                10,
                DiscountPolicy.FLAT,
                BigDecimal.valueOf(10_000),
                exdatetime
        ));
        userRepository.flush();
        couponRepository.flush();

        userId = user.getId();
        couponId = coupon.getId();
        expiredAt = exdatetime;
    }

    @Test
    void 쿠폰_발급_테스트() throws Exception{
        // Given
        CouponRequest.Issue request = CouponRequest.Issue.of(couponId, userId);

        // When & Then
        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponId").isNumber())
                .andExpect(jsonPath("$.userCouponId").isNumber())
                .andExpect(jsonPath("$.expiredAt").value(expiredAt.toString()));
    }

}
