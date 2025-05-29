package kr.hhplus.be.server.infrastructure.salesstat;

import kr.hhplus.be.server.domain.salesstat.SalesStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesStatJpaRepository extends JpaRepository<SalesStat, Long> {

    List<SalesStat> findAllBySalesDate(LocalDate targetDate, Pageable pageable);
}
