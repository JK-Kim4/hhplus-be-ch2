package kr.hhplus.be.server.infrastructure.salesStat;

import kr.hhplus.be.server.domain.salesStat.SalesStat;
import kr.hhplus.be.server.domain.salesStat.SalesStatRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Override
    public List<SalesStat> findAll() {
        return salesStatJpaRepository.findAll();
    }

    @Override
    public List<SalesStat> findAllBySalesDate(LocalDate targetDate, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("salesAmount").descending());
        return salesStatJpaRepository.findAllBySalesDate(targetDate, pageable);
    }
}
