package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item save(ItemCommand.Create command){
        Item item = Item.createWithPriceAndStock(
                command.getName(),
                command.getPrice(),
                command.getStock());
        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Item findItemById(Long itemId){
        return itemRepository.findById(itemId).orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public ItemCommand.Item findById(Long itemId){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(NoResultException::new);

        return ItemCommand.Item.from(item);
    }

    public List<Item> findByIds(List<Long> itemIds) {
        return itemRepository.findByIds(itemIds);
    }
}
