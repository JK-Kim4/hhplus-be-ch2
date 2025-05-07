package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public void flush() {
        productJpaRepository.flush();
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }


    @Override
    public Optional<Product> findByIdWithPessimisticLock(Long productId) {
        return productJpaRepository.findByIdWithPessimisticLock(productId);
    }

    @Override
    public List<Product> findByIdInWithPessimisticLock(List<Long> orderItemIds) {
        return productJpaRepository.findByIdInWithPessimisticLock(orderItemIds);
    }

    @Override
    public List<Product> findByIdIn(Set<Long> Ids) {
        return productJpaRepository.findByIdIn(Ids);
    }

    @Override
    public List<Product> findAll(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset/limit, limit, Sort.by("id").descending());
        return productJpaRepository.findAll(pageable)
                .getContent();
    }
}
