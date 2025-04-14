package kr.hhplus.be.server.domain.orderStatistics;

import java.time.LocalDate;
import java.util.List;

public class OrderStatisticsCommand {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<OrderStatistics> orderStatistics;

    public List<OrderStatistics> getOrderStatistics() {
        return orderStatistics;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public OrderStatisticsCommand(List<OrderStatistics> orderStatistics) {
        this.orderStatistics = orderStatistics;
    }

    public OrderStatisticsCommand(
            List<OrderStatistics> orderStatistics,
            LocalDate startDate, LocalDate endDate) {
        this.orderStatistics = orderStatistics;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public OrderStatisticsCommand(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
