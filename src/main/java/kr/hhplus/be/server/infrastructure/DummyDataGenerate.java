package kr.hhplus.be.server.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.instancio.Instancio;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.instancio.Select.field;

@Component
@RequiredArgsConstructor
public class DummyDataGenerate {
    private final EntityManagerFactory entityManagerFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void batch() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StatelessSession statelessSession = sessionFactory.openStatelessSession();
        Transaction tx = statelessSession.beginTransaction();

        try {
            System.out.println("data insert start");
            long before = System.currentTimeMillis();
            for (int i = 1; i < 1_000_000; i++) {
                LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
                LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59);
                User user = Instancio.of(User.class)
                        .set(field(User::getId), (long) i)
                        .create();

                statelessSession.insert(user);
            }
            tx.commit();
            long after = System.currentTimeMillis();
            long diff = after - before;
            System.out.println("data insert end: " + diff + "ms");
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            statelessSession.close();
        }
    }
}
