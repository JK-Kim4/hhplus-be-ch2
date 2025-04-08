package kr.hhplus.be.server.domain.item;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository {

    Optional<Item> findById(Long id);

}
