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
        ItemCommand.Item itemCommand = ItemCommand.Item.from(item);

        // Then
        assertEquals(item.getName(), itemCommand.getName());
    }
}
