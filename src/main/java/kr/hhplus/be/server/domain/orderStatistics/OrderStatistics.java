package kr.hhplus.be.server.domain.orderStatistics;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatistics {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oder_statistics_id")
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "order_count")
    private Integer orderCount;

    @Column
    private LocalDate statisticDate;

    public OrderStatistics(Long itemId, Integer orderCount, LocalDate statisticDate) {
        this.itemId = itemId;
        this.orderCount = orderCount;
        this.statisticDate = statisticDate;
    }

    public OrderStatistics(Long itemId, Integer orderCount) {
        this.itemId = itemId;
        this.orderCount = orderCount;
    }

    public static List<OrderStatistics> calculateOrderStatistics(List<OrderItem> orderItems, LocalDate targetDate) {
        Set<Long> itemIds = orderItems.stream().map(OrderItem::getItemId).collect(Collectors.toSet());

        List<OrderStatistics> orderStatistics = new ArrayList<>();

        for(Long itemId : itemIds) {
            OrderStatistics orderStatistic = new OrderStatistics(
                    itemId,
                    orderItems.stream()
                            .filter(orderItem -> orderItem.getItemId().equals(itemId))
                            .mapToInt(OrderItem::getQuantity)
                            .sum(),
                    targetDate);
            orderStatistics.add(orderStatistic);
        }

        return orderStatistics;
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", orderCount=" + orderCount +
                ", statisticDate=" + statisticDate +
                '}';
    }
}
