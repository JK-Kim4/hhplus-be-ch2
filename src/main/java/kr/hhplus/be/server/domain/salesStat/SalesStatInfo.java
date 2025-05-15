package kr.hhplus.be.server.domain.salesStat;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SalesStatInfo {

    @Getter
    public static class SalesStats {
        List<SalesStat> salesStats;

        public static SalesStats of(Map<Long, Long> salesStatsMap, LocalDate targetDate) {
            List<SalesStat> stats = salesStatsMap.entrySet().stream()
                    .map(entry -> SalesStat.of(entry.getKey(), targetDate, entry.getValue()))
                    .collect(Collectors.toList());

            return SalesStats.builder().salesStats(stats).build();
        }

        public static SalesStats fromRedisReports(Set<TypedScore<String>> reports){
            List<SalesStat> stats = reports.stream()
                    .map((report)-> SalesStatInfo.SalesStat.of(
                            Long.parseLong(report.value()),
                            null,
                            (long)report.score()
                    )).toList();

            return SalesStats.builder().salesStats(stats).build();

        }

        public Long getSalesAmountByProductId(Long productId){
            return this.salesStats.stream()
                    .filter(stat -> stat.getProductId().equals(productId))
                    .findFirst()
                    .map(SalesStat::getSalesAmount)
                    .orElseThrow(IllegalArgumentException::new);
        }

        @Builder
        private SalesStats(List<SalesStat> salesStats) {
            this.salesStats = salesStats;
        }
    }


    @Getter
    public static class SalesStat {
        Long productId;
        LocalDate salesDate;
        Long salesAmount;

        public static SalesStat of(Long productId, LocalDate salesDate, Long salesAmount) {
            return SalesStat.builder().productId(productId).salesDate(salesDate).salesAmount(salesAmount).build();
        }

        @Builder
        private SalesStat(Long productId, LocalDate salesDate, Long salesAmount) {
            this.productId = productId;
            this.salesDate = salesDate;
            this.salesAmount = salesAmount;
        }
    }

    @Getter
    public static class Creates {
        List<Create> creates;

        public static Creates fromList(List<SalesStat> salesStats) {
            return Creates.builder()
                        .creates(salesStats.stream().map((ss) ->
                                Create.of(
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
    public static class RedisTypedScoreSet {
        List<TypedScore<String>> typedScores;

        public static RedisTypedScoreSet of(List<TypedScore<String>> typedScores) {
            return RedisTypedScoreSet.builder().typedScores(typedScores).build();
        }

        @Builder
        private RedisTypedScoreSet(List<TypedScore<String>> typedScores) {
            this.typedScores = typedScores;
        }
    }

    @Getter
    public static class SalesReport {
        Long productId;
        LocalDate salesDate;
        Long salesAmount;

        public static SalesReport of(Long productId, LocalDate salesDate, Long salesAmount) {
            return SalesReport.builder()
                    .productId(productId)
                    .salesDate(salesDate)
                    .salesAmount(salesAmount)
                    .build();
        }

        @Builder
        private SalesReport(Long productId, LocalDate salesDate, Long salesAmount) {
            this.productId = productId;
            this.salesDate = salesDate;
            this.salesAmount = salesAmount;
        }
    }
}
