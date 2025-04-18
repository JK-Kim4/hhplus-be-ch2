package kr.hhplus.be.server.infrastructure.orderStatistics;

import kr.hhplus.be.server.domain.orderStatistics.OrderStatistics;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsCommand;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderStatisticsRepositoryImpl implements OrderStatisticsRepository {

    @Override
    public List<OrderStatistics> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<OrderStatistics> save(OrderStatisticsCommand command) {
        return List.of();
    }
}
