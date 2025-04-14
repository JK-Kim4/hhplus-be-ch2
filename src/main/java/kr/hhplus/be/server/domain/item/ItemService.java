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
}
