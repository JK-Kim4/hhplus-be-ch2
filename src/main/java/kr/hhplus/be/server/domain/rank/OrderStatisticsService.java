package kr.hhplus.be.server.domain.rank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderStatisticsService {

    private final OrderStatisticsRepository orderStatisticsRepository;

    public OrderStatisticsService(OrderStatisticsRepository orderStatisticsRepository) {
        this.orderStatisticsRepository = orderStatisticsRepository;
    }

    @Transactional
    public void save(OrderStatisticsCommand command){
        orderStatisticsRepository.save(command);
    }


    public OrderStatisticsInfo.Rank findByDateBetween(LocalDate startDate, LocalDate endDate) {
        List<OrderStatistics> byDateBetween =
                orderStatisticsRepository.findByDateBetween(startDate, endDate);
        return new OrderStatisticsInfo.Rank(byDateBetween);
    }

}
