package kr.hhplus.be.server.domain.salesstat;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesStatService {

    private final SalesStatRepository salesStatRepository;

    public SalesStatService(
            SalesStatRepository salesStatRepository) {
        this.salesStatRepository = salesStatRepository;
    }

    public void createAll(SalesStatCommand.Creates command){
        List<SalesStat> salesStats = command.getCreates().stream()
                .map((create) -> SalesStat.create(
                        create.getProductId(),
                        create.getSalesDate(),
                        create.getSalesAmount())
                )
                .toList();

        salesStatRepository.saveAll(salesStats);
    }


    public List<SalesStatInfo.SalesReport> getSalesReportsFromRedisTypedScoreSet(
            SalesStatInfo.RedisTypedScoreSet typedScoreSet,
            LocalDate targetDate) {
        return typedScoreSet.getTypedScores().stream()
                .map(report -> SalesStatInfo.SalesReport.of(
                        Long.parseLong(report.member()),
                        targetDate,
                        (long) report.score()
                )).toList();
    }
}
