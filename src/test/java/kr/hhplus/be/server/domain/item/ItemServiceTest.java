package kr.hhplus.be.server.domain.item;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.order.OrderCommand;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void 고유번호를_전달받아_상품의_상세정보를_조회할때_상품이_존재하지않으면_NoResultException(){
        //given
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        //when//then
        assertThrows(NoResultException.class, ()
                -> itemService.findItemById(1L));
    }

    @Nested
    class 주문상품_커멘드_목록을_전달받아_주문상품_생성{

        @Test
        void 커멘드가_비어있는경우_IllegalArgumentException(){
            List<OrderCommand.OrderItemCreate> emptyCommandList = new ArrayList<>();

            assertThrows(IllegalArgumentException.class, ()
                    -> itemService.getOrderItems(emptyCommandList));
        }

        @Test
        void 상품정보가_존재하지않을경우_NoResultException(){
            //given
            List<OrderCommand.OrderItemCreate> emptyCommandList = Arrays.asList(
                    new OrderCommand.OrderItemCreate(1L, 1000, 10)
            );
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            //when//then
            assertThrows(NoResultException.class, ()
                    -> itemService.getOrderItems(emptyCommandList));

        }
    }




}
