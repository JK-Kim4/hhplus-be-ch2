package kr.hhplus.be.server.domain.orderStatistics;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static class Response {

        private List<OrderStatisticsCommand.OrderRank> orderRanks;

        public List<OrderRank> getOrderRanks() {
            return orderRanks;
        }

        public Response(List<OrderStatistics> orderStatistics) {
            Map<Long, List<OrderStatistics>> grouped = orderStatistics.stream()
                    .collect(Collectors.groupingBy(OrderStatistics::getItemId));

            orderRanks = grouped.entrySet().stream()
                    .map(entry -> {
                        Long itemId = entry.getKey();
                        List<OrderStatistics> itemStats = entry.getValue();
                        String itemName = itemStats.get(0).getItemName(); // 동일 itemId는 동일 itemName 가정
                        Integer totalOrderCount = itemStats.stream().mapToInt(OrderStatistics::getOrderCount).sum();
                        return new OrderRank(itemId, itemName, totalOrderCount);
                    })
                    .sorted(Comparator.comparing(OrderRank::getOrderCount).reversed()) // 주문 수 기준 내림차순 정렬
                    .collect(Collectors.toList());
        }
    }

    public static class OrderRank{

        private Long itemId;
        private String itemName;
        private Integer orderCount;

        public Long getItemId() {
            return itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public Integer getOrderCount() {
            return orderCount;
        }

        public OrderRank(Long itemId, String itemName, Integer orderCount) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.orderCount = orderCount;
        }
    }
}
