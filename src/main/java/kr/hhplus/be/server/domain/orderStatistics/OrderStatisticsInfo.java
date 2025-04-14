package kr.hhplus.be.server.domain.orderStatistics;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderStatisticsInfo {

    public static class Rank{

        private List<OrderRank> orderRanks;

        public List<OrderRank> getOrderRanks() {
            return orderRanks;
        }

        public Rank(List<OrderStatistics> orderStatistics) {
            Map<Long, List<OrderStatistics>> grouped = orderStatistics.stream()
                    .collect(Collectors.groupingBy(OrderStatistics::getItemId));

            orderRanks = grouped.entrySet().stream()
                    .map(entry -> {
                        Long itemId = entry.getKey();
                        List<OrderStatistics> itemStats = entry.getValue();
                        Integer totalOrderCount = itemStats.stream().mapToInt(OrderStatistics::getOrderCount).sum();
                        return new OrderRank(itemId, totalOrderCount);
                    })
                    .sorted(Comparator.comparing(OrderRank::getOrderCount).reversed()) // 주문 수 기준 내림차순 정렬
                    .collect(Collectors.toList());
        }
    }

    public static class OrderRank{

        private Long itemId;
        private Integer orderCount;

        public Long getItemId() {
            return itemId;
        }

        public Integer getOrderCount() {
            return orderCount;
        }

        public OrderRank(Long itemId, Integer orderCount) {
            this.itemId = itemId;
            this.orderCount = orderCount;
        }
    }


}
