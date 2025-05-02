package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public Item deductStock(ItemCommand.Deduct command){
        Item item = itemRepository.findById(command.getItemId())
                        .orElseThrow(NoResultException::new);
        item.decreaseStock(command.getQuantity());
        return itemRepository.save(item);
    }

    public Item findItemById(Long itemId){
        return itemRepository.findById(itemId)
                .orElseThrow(NoResultException::new);
    }

    public ItemCommand.Item findById(Long itemId){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(NoResultException::new);

        return ItemCommand.Item.from(item);
    }

    public List<OrderItem> getOrderItems(List<OrderCommand.OrderItemCreate> orderItemCreateCommand) {
        if(orderItemCreateCommand.isEmpty()){
            throw new IllegalArgumentException("주문 상품이 존재하지않습니다.");
        }

        return orderItemCreateCommand.stream()
                .map(command -> OrderItem.createWithItemAndPriceAndQuantity(
                        itemRepository.findByIdWithPessimisticLock(command.getItemId())
                                .orElseThrow(NoResultException::new),
                        command.getPrice(),
                        command.getQuantity()))
                .toList();
    }
}
