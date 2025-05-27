package kr.hhplus.be.server.infrastructure.salesstat;

import kr.hhplus.be.server.common.keys.RedisKeys;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReport;
import kr.hhplus.be.server.domain.salesstat.salesReport.SalesReportInMemoryRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SalesReportRedisRepositoryImpl implements SalesReportInMemoryRepository {

    private final RedissonClient redissonClient;

    public SalesReportRedisRepositoryImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean hasKey(String key) {
        return redissonClient.getKeys().countExists(key) > 0;
    }

    @Override
    public boolean existByReportDate(LocalDate reportDate) {
        return redissonClient.getKeys().countExists(RedisKeys.DAILY_SALES_REPORT.format(reportDate)) > 0;
    }

    @Override
    public SalesReport findByReportDateAndProductId(LocalDate reportDate, Long productId) {
        RScoredSortedSet<Long> redisSalesReport = redissonClient.getScoredSortedSet(RedisKeys.DAILY_SALES_REPORT.format(reportDate));
        Double score = redisSalesReport.getScore(productId);
        return SalesReport.of(productId, score, reportDate);
    }

    @Override
    public List<SalesReport> findAllByReportDate(LocalDate reportDate) {
        RScoredSortedSet<Long> redisSalesReports = redissonClient.getScoredSortedSet(RedisKeys.DAILY_SALES_REPORT.format(reportDate));
        return redisSalesReports.entryRange(0, -1).stream()
                .map(entry -> SalesReport.of(entry.getValue(), entry.getScore(), reportDate))
                .toList();
    }

    @Override
    public void increaseDailySalesReport(LocalDate reportDate, Long productId, Double salesScore) {
        RScoredSortedSet<Long> redisSalesReport = redissonClient.getScoredSortedSet(RedisKeys.DAILY_SALES_REPORT.format(reportDate));
        redisSalesReport.addScore(productId, salesScore);
    }

    @Override
    public void deleteByReportDate(LocalDate reportDate) {
        redissonClient.getKeys().delete(RedisKeys.DAILY_SALES_REPORT.format(reportDate));
    }

    @Override
    public void setDailySalesReportKeyTTL(LocalDate localDate, Duration duration) {
        RBucket<Object> bucket = redissonClient.getBucket(RedisKeys.DAILY_SALES_REPORT.format(localDate));
        bucket.expire(duration);
    }
}
