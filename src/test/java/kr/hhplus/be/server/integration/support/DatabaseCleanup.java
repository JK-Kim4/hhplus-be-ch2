package kr.hhplus.be.server.integration.support;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseCleanup {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    // ――― 엔티티 스캔 & 테이블명 캐싱 ―――
    private List<String> getTableNames() {
        return emf.getMetamodel()
                .getEntities()
                .stream()
                .map(EntityType::getJavaType)
                .map(this::resolveTableName)
                .toList();
    }

    private String resolveTableName(Class<?> type) {
        // @Table(name = "…") 우선
        Table table = type.getAnnotation(Table.class);
        if (table != null && !table.name().isBlank())
            return camelToSnake(table.name().toLowerCase());

        // @Entity(name = "…") 그다음
        Entity entity = type.getAnnotation(Entity.class);
        if (entity != null && !entity.name().isBlank())
            return camelToSnake(entity.name().toLowerCase());

        // 없으면 클래스 이름 사용
        /* 3) 마지막으로 클래스 단순 이름을 변환 */
        return camelToSnake(type.getSimpleName());
    }

    private static String camelToSnake(String camel) {
        return camel
                .replaceAll("([a-z])([A-Z]+)", "$1_$2") // aA → a_A
                .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2") // AAa → A_Aa
                .toLowerCase();
    }

    // ――― 실제 TRUNCATE 수행 ―――
    @Transactional
    public void truncate() {
        em.flush();
        em.clear();

        List<String> tables = getTableNames();

        // 하이버네이트 Session 통해 JDBC 커넥션 직접 접근
        em.unwrap(Session.class).doWork(conn -> {
            try (Statement stmt = conn.createStatement()) {
                // 외래키 제약 해제(지원 DB 한정)
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0"); // MySQL
                // stmt.execute("SET CONSTRAINTS ALL DEFERRED"); // PostgreSQL

                for (String table : tables) {
                    stmt.execute("TRUNCATE TABLE " + table);
                }

                stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        });
    }

}
