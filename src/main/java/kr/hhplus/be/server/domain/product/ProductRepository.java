package kr.hhplus.be.server.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void flush();

    Product save(Product product);

    Optional<Product> findById(Long productId);

    List<Product> findByIdInWithPessimisticLock(List<Long> orderItemIds);
}
