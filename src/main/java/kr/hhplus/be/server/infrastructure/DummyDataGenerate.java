package kr.hhplus.be.server.infrastructure;

import lombok.Getter;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.instancio.Select.field;

@Component
public class DummyDataGenerate {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    void 사용자_포인트_데이터_등록(){
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        System.out.println("bulkInsertUserSTART");

        List<UserSave> users = Instancio.ofList(UserSave.class)
                .size(15000)
                .create();

        batchInsertUsers(users);


        List<PointSave> points = Instancio.ofList(PointSave.class)
                .size(10000)
                .withUnique(field(PointSave::getUserId))
                .generate(field(PointSave::getUserId), gen -> gen.longs().range(1L, 15000L))
                .create();

        batchInsertPoints(points);

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000;
        System.out.println("bulkInsertUserEND:  "+secDiffTime);


    }


    public void batchInsertUsers(List<UserSave> users) {
        String sql = "INSERT INTO user (name) VALUES (?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, users.get(i).getName());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    public void batchInsertPoints(List<PointSave> points) {
        String sql = "INSERT INTO point (user_id, point_amount) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, points.get(i).getUserId());
                ps.setInt(2, points.get(i).getPointAmount());
            }

            @Override
            public int getBatchSize() {
                return points.size();
            }
        });
    }



    @Getter
    private class UserSave{
        private Long id;
        private String name;

    }

    @Getter
    private class PointSave{
        private Long id;
        private Long userId;
        private Integer pointAmount;
    }
}
