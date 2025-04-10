package kr.hhplus.be.server.interfaces.api.item;

import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsCommand;
import kr.hhplus.be.server.domain.orderStatistics.OrderStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiSpec {

    private final ItemService itemService;
    private final OrderStatisticsService orderStatisticsService;

    public ItemApiController(
            ItemService itemService,
            OrderStatisticsService orderStatisticsService) {
        this.itemService = itemService;
        this.orderStatisticsService = orderStatisticsService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse.Detail> findById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(new ItemResponse.Detail(itemService.findById(id)));
    }

    @Override
    @GetMapping("/ranking")
    public ResponseEntity<ItemResponse.Rank> findItemRanking() {

        LocalDate now = LocalDate.now();
        return ResponseEntity.ok(
                new ItemResponse.Rank(
                        orderStatisticsService.findByDateBetween(
                                        new OrderStatisticsCommand(now.minusDays(1), now.minusDays(4)))
                                .getOrderRanks()));
    }
}
