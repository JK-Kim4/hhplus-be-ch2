package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public Item findById(Long itemId){
        return itemRepository.findById(itemId).orElseThrow(NoResultException::new);
    }

}
