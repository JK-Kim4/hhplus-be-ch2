package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.interfaces.api.item.ItemCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public ItemCommand.Response findById(Long itemId){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(NoResultException::new);

        return new ItemCommand.Response(item);
    }

}
