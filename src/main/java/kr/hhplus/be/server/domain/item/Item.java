package kr.hhplus.be.server.domain.item;

import java.time.LocalDateTime;

public class Item {

    private Long id;

    private String name;

    private ItemPrice price;

    private ItemStock stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
