package kr.hhplus.be.server.domain.salesstat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@Table(name = "sales_stat",
        indexes = @Index(name = "idx_join_product_id", columnList = "product_id"),
        uniqueConstraints={
                @UniqueConstraint(
                        name="unique_product_id_and_date",
                        columnNames={"product_id", "sales_date"}
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

    @Override
    public String toString() {
        return "SalesStat{" +
                "id=" + id +
                ", productId=" + productId +
                ", salesDate=" + salesDate +
                ", salesAmount=" + salesAmount +
                '}';
    }
}
