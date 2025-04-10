package kr.hhplus.be.server.domain.orderStatistics;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public OrderStatisticsCommand.Response findByDateBetween(OrderStatisticsCommand command) {

        return new OrderStatisticsCommand.Response(
                orderStatisticsRepository.findByDateBetween(command.getStartDate(), command.getEndDate()));
    }

}
