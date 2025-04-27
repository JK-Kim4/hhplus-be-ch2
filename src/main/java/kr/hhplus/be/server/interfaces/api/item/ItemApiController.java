package kr.hhplus.be.server.interfaces.api.item;

import kr.hhplus.be.server.application.item.ItemFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiSpec {

    private final ItemFacade itemFacade;

    public ItemApiController(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse.Item> findById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(ItemResponse.Item.from(itemFacade.findItemById(id)));
    }

    @Override
    @GetMapping("/rank")
    public ResponseEntity<ItemResponse.Rank> findItemRanking() {


        return ResponseEntity.ok(new ItemResponse.Rank());
    }
}
