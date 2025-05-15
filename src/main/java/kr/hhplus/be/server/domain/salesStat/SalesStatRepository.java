package kr.hhplus.be.server.domain.salesStat;

import java.time.LocalDate;
import java.util.List;

public interface SalesStatRepository {

    void flush();

    void saveAll(List<SalesStat> salesStats);

    List<SalesStat> findAllBySalesDate(LocalDate targetDate, int limit);

    List<SalesStat> findAll();

}
