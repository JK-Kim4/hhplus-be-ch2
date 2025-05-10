package kr.hhplus.be.server.domain.salesStat;

import lombok.Builder;
import lombok.Getter;

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

        public static SalesStatCommand.Creates fromList(List<SalesStatInfo.SalesStat> salesStats) {
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
}
