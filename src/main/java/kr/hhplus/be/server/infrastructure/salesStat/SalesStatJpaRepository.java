package kr.hhplus.be.server.infrastructure.salesStat;

import kr.hhplus.be.server.domain.salesStat.SalesStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesStatJpaRepository extends JpaRepository<SalesStat, Long> {
}
