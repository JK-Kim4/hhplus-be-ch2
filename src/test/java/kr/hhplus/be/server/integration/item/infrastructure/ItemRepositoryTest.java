package kr.hhplus.be.server.integration.item.infrastructure;

import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    Item item1;
    Item item2;
    Item item3;

    @BeforeEach
    void setUp() {

        item1 = Item.createWithPriceAndStock("car", 5000, 10);
        item2 = Item.createWithPriceAndStock("truck", 1000, 5);
        item3 = Item.createWithPriceAndStock("food", 2000, 3);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

    }

    @Test
    void 고유번호로_상품을_조회한다(){
        Item item = itemRepository.findById(item1.getId()).get();

        assertEquals(item.getName(), item1.getName());
    }

    @Test
    void 고유번호_목록에_해당하는_상품을_조회한다(){
        List<Long> itemIds = Arrays.asList(
                item1.getId(),
                item2.getId(),
                item3.getId(),
                99L, 999L);

        itemRepository.findByIds(itemIds);

        assertEquals(3, itemRepository.findByIds(itemIds).size());
    }

    @Test
    @Transactional
    void 상품_가격정보_수정_영속관리_테스트_1(){
        Item item = itemRepository.findById(item1.getId()).get();
        item.updatePrice(8000);

        Item udpatedItem = itemRepository.findById(item1.getId()).get();
        assertEquals(8000, udpatedItem.price());
    }

    @Test
    @Transactional
    void 상품_가격정보_수정_영속관리_테스트_2(){
        Item item = itemRepository.findById(item1.getId()).get();
        item.getPrice().updatePrice(8000);

        Item udpatedItem = itemRepository.findById(item1.getId()).get();
        assertEquals(8000, udpatedItem.price());
    }
}
