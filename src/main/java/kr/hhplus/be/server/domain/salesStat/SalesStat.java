package kr.hhplus.be.server.domain.salesStat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@Table(name = "sales_stat",
        uniqueConstraints={
                @UniqueConstraint(
                        name="contstraintName",
                        columnNames={"unique_one", "unique_two"} // DB 상의 column name 을 작성해야한다. (변수명X)
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesStat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_stats_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sales_date")
    private LocalDate salesDate;

    @Column(name = "sales_amount")
    private Long salesAmount;

    @Builder
    private SalesStat(Long productId, LocalDate salesDate, Long salesAmount) {
        this.productId = productId;
        this.salesDate = salesDate;
        this.salesAmount = salesAmount;
    }

    public static SalesStat create(Long productId, LocalDate salesDate, Long salesAmount) {
        return SalesStat.builder()
                    .productId(productId)
                    .salesDate(salesDate)
                    .salesAmount(salesAmount)
                .build();
    }
}
