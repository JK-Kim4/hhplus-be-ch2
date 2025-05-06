package kr.hhplus.be.server.infrastructure.product;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id in :ids")
    List<Product> findByIdInWithPessimisticLock(@Param("ids") List<Long> orderItemIds);
}
