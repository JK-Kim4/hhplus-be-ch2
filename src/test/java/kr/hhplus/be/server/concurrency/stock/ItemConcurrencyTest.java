package kr.hhplus.be.server.concurrency.stock;

import kr.hhplus.be.server.concurrency.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.item.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ItemConcurrencyTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    Item testItem;

    @BeforeEach
    void beforeEach() {
        Item item = ItemTestFixture.createItemFixtureWithStock(50);
        testItem = itemRepository.save(item);
        itemRepository.flush();
    }

    @Test
    void 상품_재고_차감_동시성_테스트() throws InterruptedException {

        ItemCommand.Deduction command = new ItemCommand.Deduction(testItem.getId(), 1);

        List<Runnable> tasks = List.of(
                () -> itemService.deductStock(command)
        );

        ConcurrentTestExecutor.execute(50, 50, tasks);

        itemRepository.flush();

        Item updatedItem = itemRepository.findById(testItem.getId())
                .orElseThrow();

        System.out.println("최종 상품 재고: " + updatedItem.stock());

        assertEquals(0, updatedItem.stock());
    }
}
