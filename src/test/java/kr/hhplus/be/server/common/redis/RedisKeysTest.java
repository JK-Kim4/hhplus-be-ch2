package kr.hhplus.be.server.common.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RedisKeysTest {

    @Nested
    @DisplayName("format 메소드 사용할 수 없는 KEY")
    class UnSupportedFormatMethodTest {

        @Test
        void COUPON_ACTIVE_키는_format_메소드를_사용할수없다() {
            //then
            assertThrows(UnsupportedOperationException.class, () ->
                    RedisKeys.COUPON_ACTIVE.format("format"));
        }

        @Test
        void COUPON_META_키는_format_메소드를_사용할수없다() {
            //then
            assertThrows(UnsupportedOperationException.class, () ->
                    RedisKeys.COUPON_META.format("format"));
        }
    }

    @Nested
    @DisplayName("format 메소드 사용할 수 있는 KEY")
    class SupportedFormatMethodTest {

        @Test
        void DAILY_SALES_REPORT_키는_format_메소드를_사용할수있다() {
            //given
            String expected = "products:dailySalesReport:20231001";
            //when
            String result = RedisKeys.DAILY_SALES_REPORT.format("20231001");
            //then
            assertEquals(expected, result);
        }

        @Test
        void COUPON_ISSUABLE_FLAG_키는_format_메소드를_사용할수있다() {
            //given
            String expected = "coupon:issuable:20231001";
            //when
            String result = RedisKeys.COUPON_ISSUABLE_FLAG.format("20231001");
            //then
            assertEquals(expected, result);

        }


        @Test
        void COUPON_REQUEST_ISSUE_키는_format_메소드를_사용할수있다() {
            //given
            String expected = "coupon:request:20231001";
            //when
            String result = RedisKeys.COUPON_REQUEST_ISSUE.format("20231001");
            //then
            assertEquals(expected, result);

        }

        @Test
        void LOCAL_DATE_타입파라미터는_yyyyMMdd_포맷으로_변경된다(){
            //given
            LocalDate reportDate = LocalDate.of(2023, 10, 1);
            String expected = "products:dailySalesReport:20231001";

            //when
            String result = RedisKeys.DAILY_SALES_REPORT.format(reportDate);

            //then
            assertEquals(expected, result);
        }

    }
}
