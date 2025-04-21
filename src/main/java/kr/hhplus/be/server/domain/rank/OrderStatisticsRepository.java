package kr.hhplus.be.server.domain.rank;

import java.time.LocalDate;
import java.util.List;

public interface OrderStatisticsRepository {
    List<OrderStatistics> save(OrderStatisticsCommand command);

    List<OrderStatistics> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
