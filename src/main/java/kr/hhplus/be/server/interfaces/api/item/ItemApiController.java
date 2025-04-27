package kr.hhplus.be.server.interfaces.api.item;

import kr.hhplus.be.server.domain.item.ItemService;
import kr.hhplus.be.server.domain.rank.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiSpec {

    private final ItemService itemService;
    private final RankService rankService;

    public ItemApiController(
            ItemService itemService,
            RankService rankService) {
        this.itemService = itemService;
        this.rankService = rankService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse.Item> findById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(ItemResponse.Item.from(itemService.findById(id)));
    }

//    @Override
//    @GetMapping("/rank")
//    public ResponseEntity<ItemResponse.Rank> findItemRanking() {
//        return ResponseEntity.ok(
//                new ItemResponse.Rank(orderStatisticsService.findByDateBetween(LocalDate.now().minusDays(1), LocalDate.now().minusDays(4)))
//        );
//    }
}
