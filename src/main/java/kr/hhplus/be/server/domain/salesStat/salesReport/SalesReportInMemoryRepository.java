package kr.hhplus.be.server.domain.salesStat.salesReport;

import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesReportInMemoryRepository {

    boolean hasKey(String key);

    boolean existByReportDate(LocalDate reportDate);

    SalesReport findByReportDateAndProductId(LocalDate reportDate, Long productId);

    List<SalesReport> findAllByReportDate(LocalDate reportDate);

    void increaseDailySalesReport(LocalDate reportDate, Long productId, Double salesScore);

    void deleteByReportDate(LocalDate reportDate);

    void setDailySalesReportKeyTTL(LocalDate localDate, Duration ttl);
}
