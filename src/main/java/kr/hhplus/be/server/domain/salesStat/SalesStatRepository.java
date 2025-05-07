package kr.hhplus.be.server.domain.salesStat;

import java.util.List;

public interface SalesStatRepository {

    void flush();

    void saveAll(List<SalesStat> salesStats);
}
