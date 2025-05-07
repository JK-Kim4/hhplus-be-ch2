package kr.hhplus.be.server.domain.salesStats;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesStats {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_stats_id")
    private Long id;
}
