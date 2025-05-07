package kr.hhplus.be.server.infrastructure.salesStat;

import kr.hhplus.be.server.domain.salesStat.SalesStat;
import kr.hhplus.be.server.domain.salesStat.SalesStatRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesStatRepositoryImpl implements SalesStatRepository {

    private final SalesStatJpaRepository salesStatJpaRepository;

    public SalesStatRepositoryImpl(SalesStatJpaRepository salesStatJpaRepository) {
        this.salesStatJpaRepository = salesStatJpaRepository;
    }

    @Override
    public void flush() {
        salesStatJpaRepository.flush();
    }

    @Override
    public void saveAll(List<SalesStat> salesStats) {
        salesStatJpaRepository.saveAll(salesStats);
    }
}
