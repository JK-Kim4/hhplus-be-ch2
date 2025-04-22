package kr.hhplus.be.server.integration.support;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleanup {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void truncateAllTables() {

        entityManager.createNativeQuery("TRUNCATE TABLE user").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE point").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE orders").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE payment").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE user_coupon").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE coupon").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE item").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE order_item").executeUpdate();

    }

}
