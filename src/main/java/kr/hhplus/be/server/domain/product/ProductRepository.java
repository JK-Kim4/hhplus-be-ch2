package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository {
    
    Product save(Product product);

    Optional<Product> findById(Long productId);
}
