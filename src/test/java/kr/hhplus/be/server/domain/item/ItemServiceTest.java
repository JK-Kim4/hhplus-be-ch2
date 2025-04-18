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

}
