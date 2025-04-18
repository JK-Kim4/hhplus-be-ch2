package kr.hhplus.be.server.domain.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findByIds(List<Long> itemIds);

    void deleteAll();

}
