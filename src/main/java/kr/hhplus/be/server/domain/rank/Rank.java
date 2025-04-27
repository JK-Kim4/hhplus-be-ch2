package kr.hhplus.be.server.domain.rank;

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

@Entity(name = "ranks")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "order_count")
    private Integer orderCount;

    @Column
    private LocalDate orderDate;

    public Rank(Long itemId, Integer orderCount, LocalDate orderDate) {
        this.itemId = itemId;
        this.orderCount = orderCount;
        this.orderDate = orderDate;
    }

    public Rank(Long itemId, Integer orderCount) {
        this.itemId = itemId;
        this.orderCount = orderCount;
    }

    public static List<Rank> calculateOrderStatistics(List<OrderItem> orderItems, LocalDate targetDate) {
        Set<Long> itemIds = orderItems.stream().map(OrderItem::getItemId).collect(Collectors.toSet());

        List<Rank> orderStatistics = new ArrayList<>();

        for(Long itemId : itemIds) {
            Rank orderStatistic = new Rank(
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

    public static List<Rank> calculate(List<OrderItem> orderItemList, LocalDate targetDate) {
        Rank.calculateOrderStatistics(orderItemList, targetDate);

        List<Rank> ranks = new ArrayList<>();

        Set<Long> itemIds = orderItemList.stream()
                .map(OrderItem::getItemId)
                .collect(Collectors.toSet());

        for(Long id : itemIds) {
            Rank rank = new Rank(
                    id,
                    orderItemList.stream()
                        .filter(orderItem -> orderItem.getItemId().equals(id))
                        .mapToInt(OrderItem::getQuantity)
                        .sum(),
                    targetDate);

            ranks.add(rank);
        }

        return ranks;
    }

    private static void validation(List<OrderItem> orderItems, LocalDate targetDate) {
        Set<LocalDate> orderDate = orderItems.stream()
                .map(orderItem -> orderItem.getOrder().getOrderDate())
                .collect(Collectors.toSet());

        if(orderDate.size() > 1) {
            throw new IllegalArgumentException("상품 판매 통계 정보가 올바르지않습니다.");
        }

        if(!orderDate.contains(targetDate)){
            throw new IllegalArgumentException("통계 날짜와 상품 판매 날짜가 동일하지 않습니다.");
        }

    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", orderCount=" + orderCount +
                ", orderDate=" + orderDate +
                '}';
    }
}
