package kr.hhplus.be.server.interfaces.api.item;

import kr.hhplus.be.server.domain.item.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiSpec{

    private final ItemService itemService;

    public ItemApiController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse.Detail> findById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(new ItemResponse.Detail(itemService.findById(id)));
    }

    @Override
    @GetMapping("/ranking")
    public ResponseEntity<List<ItemResponse.Rank>> findItemRanking() {
        return null;
    }
}
