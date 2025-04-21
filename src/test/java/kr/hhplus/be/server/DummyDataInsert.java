package kr.hhplus.be.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class DummyDataInsert {

    @Autowired
    JdbcTemplate jdbcTemplate;


}
