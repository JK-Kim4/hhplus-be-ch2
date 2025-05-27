package kr.hhplus.be.server.domain.salesstat.salesReport;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesReportCommand {

    private static final DateTimeFormatter SALES_REPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Getter
    public static class ReportDate {
        LocalDate  reportDate;

        public static ReportDate of(LocalDate reportDate) {
            return ReportDate.builder()
                    .reportDate(reportDate)
                    .build();
        }


        @Builder
        private ReportDate(LocalDate reportDate) {
            this.reportDate = reportDate;
        }
    }

    @Getter
    public static class SalesReport {
        LocalDate reportDate;
        Long productId;

        public static SalesReport of(LocalDate reportDate, Long productId) {
            return SalesReport.builder()
                    .reportDate(reportDate)
                    .productId(productId)
                    .build();
        }
        @Builder
        private SalesReport(LocalDate reportDate, Long productId) {
            this.reportDate = reportDate;
            this.productId = productId;
        }
    }

    @Getter
    public static class IncreaseSalesReport {
        LocalDate reportDate;
        Long productId;
        Double salesScore;

        public static IncreaseSalesReport of(LocalDate reportDate, Long productId, Double salesScore) {
            return IncreaseSalesReport.builder()
                    .reportDate(reportDate)
                    .productId(productId)
                    .salesScore(salesScore)
                    .build();
        }

        @Builder
        private IncreaseSalesReport(LocalDate reportDate, Long productId, Double salesScore) {
            this.reportDate = reportDate;
            this.productId = productId;
            this.salesScore = salesScore;
        }
    }

    @Getter
    public static class SalesReportTTL{
        LocalDate reportDate;
        Duration duration;


        public static SalesReportTTL of(LocalDate reportDate, Duration duration){
            return SalesReportTTL.builder()
                    .reportDate(reportDate)
                    .duration(duration)
                    .build();
        }

        @Builder
        private SalesReportTTL(LocalDate reportDate, Duration duration){
            this.reportDate = reportDate;
            this.duration = duration;
        }
    }
}
