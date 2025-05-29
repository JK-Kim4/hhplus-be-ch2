package kr.hhplus.be.server.domain.retryfailedlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.StringUtil;
import kr.hhplus.be.server.common.keys.CacheKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetryFailedLogService {

    private final Logger logger = LoggerFactory.getLogger(RetryFailedLogService.class);

    private final RetryFailedLogRepository retryFailedLogRepository;
    private final ObjectMapper objectMapper;

    public RetryFailedLogService(
            RetryFailedLogRepository retryFailedLogRepository,
            ObjectMapper objectMapper) {
        this.retryFailedLogRepository = retryFailedLogRepository;
        this.objectMapper = objectMapper;
    }

    public void save(RetryFailedLog log){
        retryFailedLogRepository.save(CacheKeys.FAILED_RETRY_LOG.name(), StringUtil.toJson(log));
    }

    public List<RetryFailedLog> findAll() {
        List<String> jsonLogs = retryFailedLogRepository.findAllRowJsonByKey(CacheKeys.FAILED_RETRY_LOG.name());
        return jsonLogs.stream()
                .map((value) -> StringUtil.deserializeSafely(value, RetryFailedLog.class))
                .toList();
    }


}
