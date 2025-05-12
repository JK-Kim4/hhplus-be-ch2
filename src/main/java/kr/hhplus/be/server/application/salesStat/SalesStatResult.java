package kr.hhplus.be.server.application.salesStat;

import kr.hhplus.be.server.domain.salesStat.SalesStatCommand;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesStatResult {

    @Getter
    public static class SalesStats {
        List<SalesStat> salesStats;

        public SalesStatCommand.Creates toCreatesCommand() {
            return SalesStatCommand.Creates.fromResultList(this.salesStats);
        }

        public static SalesStats of(Map<Long, Long> salesStatsMap, LocalDate targetDate) {
            List<SalesStatResult.SalesStat> stats = salesStatsMap.entrySet().stream()
                    .map(entry -> SalesStatResult.SalesStat.of(entry.getKey(), targetDate, entry.getValue()))
                    .collect(Collectors.toList());

            return SalesStats.builder().salesStats(stats).build();

        }

        @Builder
        private SalesStats(List<SalesStatResult.SalesStat> salesStats) {
            this.salesStats = salesStats;
        }
    }


    @Getter
    public static class SalesStat {
        Long productId;
        LocalDate salesDate;
        Long salesAmount;

        public static SalesStatResult.SalesStat of(Long productId, LocalDate salesDate, Long salesAmount) {
            return SalesStat.builder().productId(productId).salesDate(salesDate).salesAmount(salesAmount).build();
        }

        @Builder
        private SalesStat(Long productId, LocalDate salesDate, Long salesAmount) {
            this.productId = productId;
            this.salesDate = salesDate;
            this.salesAmount = salesAmount;
        }


    }
}
