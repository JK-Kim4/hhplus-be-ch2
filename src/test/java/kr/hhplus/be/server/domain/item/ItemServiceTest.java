package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    private ItemRepository itemRepository;
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemService(itemRepository);
    }

    @Test
    void testFindById_success() {
        // given
        Long itemId = 1L;
        Item mockItem = mock(Item.class);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));
        ItemCommand.Item expected = ItemCommand.Item.from(mockItem);

        // when
        ItemCommand.Item result = itemService.findById(itemId);

        // then
        assertEquals(expected, result);
    }

    @Test
    void testFindById_notFound() {
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NoResultException.class, () -> itemService.findById(itemId));
    }

    @Test
    void testCanOrder_success() {
        // given
        Long itemId = 1L;
        int quantity = 5;
        int price = 1000;

        ItemCommand.CanOrder command = new ItemCommand.CanOrder(itemId, price, quantity);
        Item mockItem = mock(Item.class);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        // when
        itemService.canOrder(command);

        // then
        verify(mockItem).isSamePrice(price);
        verify(mockItem).hasEnoughStock(quantity);
    }

    @Test
    void testCanOrder_itemNotFound() {
        Long itemId = 1L;
        ItemCommand.CanOrder command = new ItemCommand.CanOrder(itemId, 5, 1000);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NoResultException.class, () -> itemService.canOrder(command));
    }

    @Test
    void testDecreaseStock_success() {
        Long itemId = 1L;
        int quantity = 3;

        ItemCommand.DecreaseStock command = new ItemCommand.DecreaseStock(itemId, quantity);
        Item mockItem = mock(Item.class);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        itemService.decreaseStock(command);

        verify(mockItem).decreaseStock(quantity);
    }

    @Test
    void testDecreaseStock_itemNotFound() {
        Long itemId = 1L;
        ItemCommand.DecreaseStock command = new ItemCommand.DecreaseStock(itemId, 3);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NoResultException.class, () -> itemService.decreaseStock(command));
    }

}
