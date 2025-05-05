package kr.hhplus.be.server.domain.product;

import java.util.Optional;

public interface ProductRepository {

    void flush();

    Product save(Product product);

    Optional<Product> findById(Long productId);
}
