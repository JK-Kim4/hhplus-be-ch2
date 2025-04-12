package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.interfaces.api.item.ItemApiController;
import kr.hhplus.be.server.interfaces.api.item.ItemResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ItemApiControllerTest {

    @Autowired
    ItemApiController itemApiController;

    @BeforeEach
    public void init(){}

    @DisplayName("상품 조회 요청 성공 (HTTP STATUS 200)")
    @Test
    void test(){
        ResponseEntity<ItemResponse> byId = itemApiController.findById(1L);
        Assertions.assertEquals(HttpStatus.OK, byId.getStatusCode());
    }
}
