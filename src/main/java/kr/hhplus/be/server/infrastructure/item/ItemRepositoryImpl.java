package kr.hhplus.be.server.infrastructure.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.domain.item.Item;
import kr.hhplus.be.server.domain.item.ItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @PersistenceContext
    private EntityManager em;

    private final ItemJpaRepository itemJpaRepository;
    public ItemRepositoryImpl(ItemJpaRepository itemJpaRepository) {
        this.itemJpaRepository = itemJpaRepository;
    }

    @Override
    public Item save(Item item) {
        return itemJpaRepository.save(item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemJpaRepository.findById(id);
    }

    @Override
    public List<Item> findByIds(List<Long> itemIds) {
        return itemJpaRepository.findAllById(itemIds);
    }

    @Override
    public void deleteAll() {
        itemJpaRepository.deleteAll();
    }
}
