package kr.hhplus.be.server.e2e;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.user.User;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    InitialTestData initialTestData;

    SampleValues sampleValues;

    @BeforeEach
    void setUp() {
        databaseCleanup.truncateAllTables();
        sampleValues = initialTestData.load();
    }

    @Test
    void 사용자는_쿠폰을_발급받을수있다() throws Exception {
        //when
        mockMvc.perform(post("/api/coupons/"+sampleValues.coupon.getId()+"/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleValues.user.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void 발급받은_쿠폰의_목록을_조회할수있다() throws Exception {
        //given
        User user = sampleValues.user;
        UserCoupon userCoupon = sampleValues.coupon.issue(user, LocalDate.now());
        userCouponRepository.save(userCoupon);

        //when//then
        mockMvc.perform(get("/api/coupons/user/{uerId}", user.getId()))
                .andExpect(status().isOk());
    }


}
