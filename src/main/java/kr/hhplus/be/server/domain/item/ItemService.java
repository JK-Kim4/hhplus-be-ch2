package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item findItemById(Long itemId){
        return itemRepository.findById(itemId).orElseThrow(NoResultException::new);
    }

    @Transactional(readOnly = true)
    public ItemCommand.Item findById(Long itemId){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(NoResultException::new);

        return ItemCommand.Item.from(item);
    }

    public void canOrder(ItemCommand.CanOrder command) {

        Item item = itemRepository.findById(command.getItemId())
                .orElseThrow(NoResultException::new);

        item.isSamePrice(command.getPrice());
        item.hasEnoughStock(command.getQuantity());
    }

    public void decreaseStock(ItemCommand.DecreaseStock command) {
        Item item = itemRepository.findById(command.getItemId())
                .orElseThrow(NoResultException::new);

        item.decreaseStock(command.getQuantity());
    }

    public List<Item> findByIds(List<Long> itemIds) {
        return itemRepository.findByIds(itemIds);
    }
}
