package kr.hhplus.be.server.domain.salesstat.salesReport;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @NoArgsConstructor
public class SalesReport implements Comparable<SalesReport>{
    private Long productId;
    private Double salesScore;
    private LocalDate reportDate;

    @Builder
    private SalesReport(Long productId, Double salesScore, LocalDate reportDate) {
        this.productId = productId;
        this.salesScore = salesScore;
        this.reportDate = reportDate;
    }

    public static SalesReport of(Long productId, Double score, LocalDate reportDate) {
        return SalesReport.builder().productId(productId).salesScore(score).reportDate(reportDate).build();
    }

    @Override
    public int compareTo(SalesReport other) {
        if (this == other) return 0;
        return Double.compare(other.salesScore, this.salesScore);
    }
}
