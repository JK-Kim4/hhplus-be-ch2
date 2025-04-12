package kr.hhplus.be.server.domain.orderStatistics;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderStatisticsRepository {
    List<OrderStatistics> save(OrderStatisticsCommand command);

    List<OrderStatistics> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
