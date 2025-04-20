package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.command.OrderCommand;
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
        Item item = Item.createWithNameAndPriceAndStock(
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

    public List<OrderItem> getOrderItems(List<OrderCommand.OrderItemCreate> orderItemCreateCommand) {
        if(orderItemCreateCommand.isEmpty()){
            throw new IllegalArgumentException("주문 상품이 존재하지않습니다.");
        }

        return orderItemCreateCommand.stream()
                .map(command -> OrderItem.createWithItemAndPriceAndQuantity(
                        itemRepository.findById(command.getItemId())
                                .orElseThrow(NoResultException::new),
                        command.getPrice(),
                        command.getQuantity()))
                .toList();
    }
}
