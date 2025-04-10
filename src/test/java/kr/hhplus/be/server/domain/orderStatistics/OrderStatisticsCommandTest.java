package kr.hhplus.be.server.domain.orderStatistics;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class OrderStatisticsCommandTest {

    @Test
    void responseConstructor_shouldGroupAndSortOrderStatisticsCorrectly() {
        // given
        List<OrderStatistics> statistics = List.of(
                new OrderStatistics(100L, "Item A", 3, LocalDate.of(2024, 4, 7)),
                new OrderStatistics(101L, "Item B", 2, LocalDate.of(2024, 4, 8)),
                new OrderStatistics(101L, "Item B", 2, LocalDate.of(2024, 4, 7)),
                new OrderStatistics(102L, "Item C", 7, LocalDate.of(2024, 4, 9))
        );

        // when
        OrderStatisticsCommand.Response response = new OrderStatisticsCommand.Response(statistics);
        List<OrderStatisticsCommand.OrderRank> ranks = response.getOrderRanks();

        // then
        assertThat(ranks).hasSize(3);

        OrderStatisticsCommand.OrderRank topRank = ranks.get(0);
        OrderStatisticsCommand.OrderRank secondRank = ranks.get(1);

        assertThat(topRank.getItemId()).isEqualTo(102L);
        assertThat(topRank.getItemName()).isEqualTo("Item C");
        assertThat(topRank.getOrderCount()).isEqualTo(7L);

        assertThat(secondRank.getItemId()).isEqualTo(101L);
        assertThat(secondRank.getItemName()).isEqualTo("Item B");
        assertThat(secondRank.getOrderCount()).isEqualTo(4L);
    }
}
