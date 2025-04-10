package kr.hhplus.be.server.domain.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ItemCommandTest {

    @Test
    @DisplayName("Item을 전달받아 Item Command Response 객체를 생성합니다.")
    void constructor_setsItemCorrectly() {
        // Given
        Item item = new Item("Test Item", 1000, 10);

        // When
        ItemCommand.Response response = new ItemCommand.Response(item);

        // Then
        assertNotNull(response.getItem());
        assertEquals(item, response.getItem());
    }

    @Test
    @DisplayName("Item Command Resonse의 Item 객체와 생성된 Item 객체는 동일합니다.")
    void getItem_returnsCorrectItem() {
        // Given
        Item item = new Item("Another Item", 5000, 5);
        ItemCommand.Response response = new ItemCommand.Response(item);

        // When
        Item result = response.getItem();

        // Then
        assertSame(item, result);
    }
}
