package kr.hhplus.be.server.controller.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiSpec{

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(new ItemResponse());
    }

    @Override
    @GetMapping("/ranking")
    public ResponseEntity<List<ItemRankResponse>> findItemRanking() {
        return null;
    }
}
