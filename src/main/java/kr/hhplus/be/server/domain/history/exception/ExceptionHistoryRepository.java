package kr.hhplus.be.server.domain.history.exception;

import java.time.LocalDate;
import java.util.Set;

public interface ExceptionHistoryRepository {

    void save(LocalDate exceptionDate, ExceptionHistory exceptionHistory);

    Set<ExceptionHistory> findByDate(LocalDate exceptionDate);

}
