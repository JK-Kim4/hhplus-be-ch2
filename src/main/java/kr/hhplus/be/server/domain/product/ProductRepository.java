package kr.hhplus.be.server.domain.product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository {

    void flush();

    Product save(Product product);

    Optional<Product> findById(Long productId);

    Optional<Product> findByIdWithPessimisticLock(Long productId);

    List<Product> findByIdInWithPessimisticLock(List<Long> orderItemIds);

    List<Product> findByIdIn(Set<Long> longs);

    List<Product> findByIdIn(List<Long> longs);

    List<Product> findAll(Integer offset, Integer limit);
}
