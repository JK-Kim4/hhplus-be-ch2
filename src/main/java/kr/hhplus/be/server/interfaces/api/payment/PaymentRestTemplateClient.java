package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.interfaces.RestTemplateClient;
import org.springframework.stereotype.Component;

@Component
public class PaymentRestTemplateClient implements RestTemplateClient {

    @Override
    public <T> T post(String url, Object request, Class<T> responseType) {
        return null;
    }
}
