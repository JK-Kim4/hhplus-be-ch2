package kr.hhplus.be.server.domain.orderStatistics;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.order.orderItem.OrderItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderStatistics {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer orderCount;
    private LocalDate statisticDate;

    public OrderStatistics(){}

    public OrderStatistics(Long itemId, String itemName, Integer orderCount, LocalDate statisticDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.orderCount = orderCount;
        this.statisticDate = statisticDate;
    }

    public OrderStatistics(Long itemId, String itemName, Integer orderCount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.orderCount = orderCount;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public LocalDate getStatisticDate() {
        return statisticDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public static List<OrderStatistics> calculateOrderStatistics(List<OrderItem> orderItems, LocalDate targetDate) {
        Set<Item> items = orderItems.stream().map(OrderItem::getItem).collect(Collectors.toSet());

        List<OrderStatistics> orderStatistics = new ArrayList<>();

        for(Item item : items) {
            OrderStatistics orderStatistic = new OrderStatistics(item.getId(), item.getName(),
                    orderItems.stream()
                            .filter(orderItem -> orderItem.getItem().getId().equals(item.getId()))
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
                ", itemName='" + itemName +
                ", orderCount=" + orderCount +
                ", statisticDate=" + statisticDate +
                '}';
    }
}
