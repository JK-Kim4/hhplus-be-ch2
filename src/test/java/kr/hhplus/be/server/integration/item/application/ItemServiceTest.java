package kr.hhplus.be.server.integration.item.application;

import kr.hhplus.be.server.domain.item.*;
import kr.hhplus.be.server.interfaces.common.exception.InvalidPriceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Testcontainers
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    Item item1;
    Item item2;
    Item item3;

    @BeforeEach
    void setUp() {
        item1 = itemRepository.save(Item.createWithNameAndPrice("item", 5_000));
        item2 = itemRepository.save(Item.createWithNameAndPriceAndStock("car", 1_000, 50));
        item3 = itemRepository.save(Item.createWithNameAndPriceAndStock("food", 12_000, 50));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    void 상품을_등록한다(){
        item1 = itemRepository.save(item1);

        Long savedItemId = itemService.findItemById(item1.getId()).getId();
        Item savedItem = itemService.findItemById(savedItemId);

        assertEquals(item1, savedItem);
    }

    @Test
    void 상품을_조회한다(){
        Item savedItem1 = itemRepository.findById(item1.getId()).get();

        assertEquals(item1, savedItem1);
    }

    @Test
    void 상품_목록을_조회한다(){
        List<Long> ids = Arrays.asList(item1.getId(), item2.getId());

        List<Item> items = itemRepository.findByIds(ids);

        assertEquals(2, items.size());
    }

    @Test
    void 상품_판매금액이_100원_미만일경우_오류발생(){
        String name = "truck";
        Integer price = Price.MINIMUM_ITEM_PRICE-1;
        Integer stock = 100;
        ItemCommand.Create itemCommand = ItemCommand.Create.of(name, price, stock);

        assertThrows(InvalidPriceException.class, () -> itemService.save(itemCommand));
    }


}
