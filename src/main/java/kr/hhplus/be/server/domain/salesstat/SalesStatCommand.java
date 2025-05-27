package kr.hhplus.be.server.domain.salesstat;

import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.salesstat.SalesStatResult;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReport;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class SalesStatCommand {

    @Getter
    public static class SalesStats {
        LocalDate targetDate;

        public static SalesStats of(LocalDate targetDate) {
            return SalesStats.builder().targetDate(targetDate).build();
        }

        @Builder
        private SalesStats(LocalDate targetDate) {
            this.targetDate = targetDate;
        }

    }

    @Getter
    public static class Creates {
        List<Create> creates;

        public static Creates fromReportList(List<SalesReport> reports) {
            return Creates.builder()
                    .creates(reports.stream().map( report ->
                            SalesStatCommand.Create.of(
                                    report.getProductId(),
                                    report.getReportDate(),
                                    report.getSalesScore().longValue()
                            )
                    ).toList()).build();
        }

        public static SalesStatCommand.Creates fromResultList(List<SalesStatResult.SalesStat> salesStats){
            return Creates.builder()
                    .creates(salesStats.stream().map( (ss) ->
                        SalesStatCommand.Create.of(
                                ss.getProductId(),
                                ss.getSalesDate(),
                                ss.getSalesAmount()
                        )).toList()).build();
        }

        public static SalesStatCommand.Creates fromList(List<SalesStatInfo.SalesReport> salesStats) {
            return SalesStatCommand.Creates.builder()
                    .creates(salesStats.stream().map((ss) ->
                            SalesStatCommand.Create.of(
                                    ss.productId,
                                    ss.salesDate,
                                    ss.salesAmount)
                    ).toList())
                    .build();
        }

        @Builder
        private Creates(List<Create> creates) {
            this.creates = creates;
        }
    }

    @Getter
    public static class Create {
        Long productId;
        LocalDate salesDate;
        Long salesAmount;

        public static Create of(Long productId, LocalDate salesDate, Long salesAmount) {
            return Create.builder()
                        .productId(productId)
                        .salesDate(salesDate)
                        .salesAmount(salesAmount)
                    .build();
        }

        @Builder
        private Create(Long productId, LocalDate salesDate, Long salesAmount) {
            this.productId = productId;
            this.salesDate = salesDate;
            this.salesAmount = salesAmount;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RedisAddSortedSet {
        @NotNull(message = "Redis 입력 정보가 누락되었습니다. (key)")
        String key;
        @NotNull(message = "Redis 입력 정보가 누락되었습니다. (value)")
        String value;
        @NotNull(message = "Redis 입력 정보가 누락되었습니다. (score)")
        double score;

        public static RedisAddSortedSet of(String key, String value, double score) {
            return RedisAddSortedSet.builder().key(key).value(value).score(score).build();
        }

        @Builder
        private RedisAddSortedSet(String key, String value, double score) {
            this.key = key;
            this.value = value;
            this.score = score;
        }
    }

    @Getter
    public static class RedisDeleteKey {
        String key;

        public static RedisDeleteKey of(String key) {
            return RedisDeleteKey.builder().key(key).build();
        }

        @Builder
        private RedisDeleteKey(String key) {
            this.key = key;
        }
    }
}
